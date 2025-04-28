package com.libbib.spewnikpl.presentation.SongListFragment

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.libbib.spewnikpl.R
import com.libbib.spewnikpl.databinding.FragmentSongListBinding
import com.libbib.spewnikpl.di.SpewnikApplication
import com.libbib.spewnikpl.di.ViewModelFactory
import com.libbib.spewnikpl.domain.Song
import com.libbib.spewnikpl.domain.SongType
import com.libbib.spewnikpl.presentation.SongFragment.SongFragment
import com.libbib.spewnikpl.presentation.SongFragment.SongFragment.Companion.SONG_FRAGMENT_BACK_STACK_NAME
import kotlinx.coroutines.launch
import javax.inject.Inject


class SongListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var adapter: SongListAdapter

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SongListViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as SpewnikApplication).component
    }

    private var _binding: FragmentSongListBinding? = null
    private val binding: FragmentSongListBinding
        get() = _binding ?: throw Exception("FragmentSongListBinding is null")

    private lateinit var typesBtn: List<TextView>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSongListBinding.inflate(layoutInflater)
        typesBtn =
            listOf(
                binding.allBtn,
                binding.partsOfMessBtn,
                binding.longBtn,
                binding.shortBtn,
                binding.carolsBtn
            )
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setupDrawerLayout()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        setupRecyclerView()
        observeViewModel()
        setOnClickListeners()
        setOnTextChangedListener()
    }


    private fun setOnTextChangedListener() {
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.searchInList(binding.searchEt.text.toString())
                binding.recyclerView.scrollToPosition(0)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun setOnClickListeners() {
        for (textView in typesBtn) {
            textView.setOnClickListener {
                sortSongListByType(textView)
            }
        }
    }

    private fun setupDrawerLayout() {
        if (isPortraitScapeMode()) {
            enableDrawerLayout()
        } else {
            disableDrawerLayout()
        }
    }

    private fun enableDrawerLayout() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        binding.menubtn.setOnClickListener {
            binding.drawerLayout.open()
        }
        binding.optionsTv.setOnClickListener {
            findNavController().navigate(
                SongListFragmentDirections.actionSongListFragmentToOptionsFragment()
            )
        }
        binding.mainTv.setOnClickListener {
            binding.drawerLayout.close()
        }
        binding.randomSongTv.setOnClickListener {
            adapter.launchRandomSong()
        }
        binding.infoTv.setOnClickListener {
            findNavController().navigate(
                SongListFragmentDirections.actionSongListFragmentToAboutUsFragment()
            )
        }
    }

    private fun disableDrawerLayout() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun sortSongListByType(i: TextView) {
        viewModel.sortSongListByType(i.text.toString())

    }


    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is SongListFragmentState.Content -> {
                        adapter.submitList(it.songList)
                        binding.listProgressBar.visibility = View.INVISIBLE
                        colorTypeTextViews(it.currentSongType)
                    }

                    is SongListFragmentState.Progress -> {
                        binding.listProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun colorTypeTextViews(type: SongType) {
        val textViewClicked = when (type) {
            SongType.ALL -> binding.allBtn
            SongType.LONG -> binding.longBtn
            SongType.SHORT -> binding.shortBtn
            SongType.PART_OF_MASS -> binding.partsOfMessBtn
            SongType.CAROLS -> binding.carolsBtn
        }
        textViewClicked.setTextColor(
            ContextCompat.getColor(requireActivity(), R.color.C2)
        )
        for (notClickedTv in typesBtn) {
            if (notClickedTv != textViewClicked) {
                notClickedTv.setTextColor(
                    ContextCompat.getColor(requireActivity(), R.color.C4)
                )
            }
        }
    }

    private fun setupRecyclerView() {
        adapter.onSongItemClickListener = {
            launchSongFragment(it)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun launchSongFragment(it: Song) {
        if (isPortraitScapeMode()) {
            launchInPortraitMode(it)
        } else {
            launchWithLandscapeMode(it)
        }
    }

    private fun isPortraitScapeMode() =
        resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    private fun launchWithLandscapeMode(it: Song) {
        val fragment = SongFragment.newInstance(it.id)
        requireActivity().supportFragmentManager.popBackStack(
            SONG_FRAGMENT_BACK_STACK_NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerViewSongText, fragment)
            .addToBackStack(SONG_FRAGMENT_BACK_STACK_NAME)
            .commit()
    }

    private fun launchInPortraitMode(it: Song) {
        findNavController().navigate(
            SongListFragmentDirections.actionSongListFragmentToSongFragment(
                it.id
            )
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}