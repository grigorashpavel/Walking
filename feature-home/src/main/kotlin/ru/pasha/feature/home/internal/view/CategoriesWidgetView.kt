package ru.pasha.feature.home.internal.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import ru.pasha.feature.home.R
import ru.pasha.feature.home.databinding.CategoriesWidgetBinding
import ru.pasha.feature.home.databinding.TabUserCategoriesBinding

class CategoriesWidgetView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: CategoriesWidgetBinding =
        CategoriesWidgetBinding.inflate(LayoutInflater.from(context), this, true)

    private val tabListener = object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            if (tab != null) {
                tab.text = when (binding.homeCategoryPager.adapter?.getItemViewType(tab.position)) {
                    R.layout.tab_user_categories -> "Ваши настройки"
                    R.layout.tab_default_category -> "Стандартный"
                    else -> ""
                }
            }
            val state = if (tab?.position == 0) State.User(null) else State.Default
            stateListener?.invoke(state)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

        override fun onTabReselected(tab: TabLayout.Tab?) = Unit
    }

    private val onSelectCategory: (Category) -> Unit = { category ->
        stateListener?.invoke(State.User(category))
    }

    private var currentState: State? = null

    private var stateListener: ((State) -> Unit)? = null

    private var inProgress = false

    init {
        setupViewPager()
        setupTabLayout()
    }

    fun setStateListener(listener: (State) -> Unit) {
        stateListener = listener
    }

    fun setState(state: State) {
        if (currentState == state || inProgress) return

        inProgress = true
        currentState = state
        render(state)
        inProgress = false
    }

    private fun setupViewPager() {
        binding.homeCategoryPager.adapter = CategoryPagerAdapter(
            onCategorySelected = onSelectCategory
        )
    }

    private fun setupTabLayout() {
        TabLayoutMediator(
            binding.homeCategoriesTabWidget,
            binding.homeCategoryPager,
            false
        ) { tab, _ ->
//            tab.text = when (binding.homeCategoryPager.adapter?.getItemViewType(tab.position)) {
//                R.layout.tab_user_categories -> "Ваши настройки"
//                R.layout.tab_default_category -> "Стандартный"
//                else -> ""
//            }
        }.attach()
        binding.homeCategoriesTabWidget.addOnTabSelectedListener(tabListener)
    }

    private fun render(state: State) {
        when (state) {
            State.Default -> {
                binding.homeCategoriesTabWidget.selectTab(
                    binding.homeCategoriesTabWidget.getTabAt(1)
                )
            }

            is State.User -> {
                binding.homeCategoriesTabWidget.selectTab(
                    binding.homeCategoriesTabWidget.getTabAt(0)
                )
            }
        }
        if (state !is State.User || state.category == null) return
        (binding.homeCategoryPager.adapter as? CategoryPagerAdapter)?.setCategory(state.category)
    }

    private class CategoryPagerAdapter(
        private val onCategorySelected: (Category) -> Unit
    ) : RecyclerView.Adapter<CategoryPagerAdapter.ViewHolder>() {
        private var newCategory: Category? = null

        @SuppressLint("NotifyDataSetChanged")
        fun setCategory(category: Category) {
            newCategory = category
            notifyItemChanged(0)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return ViewHolder(inflater.inflate(viewType, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (getItemViewType(position) == R.layout.tab_user_categories) {
                setupUserCategories(holder.itemView)
            }
        }

        private fun setupUserCategories(view: View) {
            val binding = TabUserCategoriesBinding.bind(view)

            binding.natureRadio.isChecked = newCategory == Category.NATURE
            binding.residentialRadio.isChecked = newCategory == Category.RESIDENTIAL
            binding.roadsRadio.isChecked = newCategory == Category.ROADS

            binding.natureCard.setOnClickListener {
                onCategorySelected(Category.NATURE)
            }
            binding.residentialCard.setOnClickListener {
                onCategorySelected(Category.RESIDENTIAL)
            }
            binding.roadsCard.setOnClickListener {
                onCategorySelected(Category.ROADS)
            }
        }

        override fun getItemCount(): Int = 2

        override fun getItemViewType(position: Int): Int = when (position) {
            0 -> R.layout.tab_user_categories
            else -> R.layout.tab_default_category
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    }

    sealed class State {
        data class User(val category: Category?) : State()
        data object Default : State()
    }

    enum class Category { NATURE, RESIDENTIAL, ROADS }
}
