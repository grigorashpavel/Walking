package ru.pasha.common.extensions

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.TextView
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import ru.pasha.common.Text
import ru.pasha.common.format

fun TextView.setText(text: Text) = setText(text.format(context))

fun View.safeAddOnGlobalLayoutListener(listener: OnGlobalLayoutListener): AutoCloseable {
    return DetachingViewListener(
        this,
        add = { it.viewTreeObserver.addOnGlobalLayoutListener(listener) },
        remove = { it.viewTreeObserver.removeOnGlobalLayoutListener(listener) },
    ).init()
}

fun View.globalLayouts(): Flow<View> = callbackFlow {
    val listener = OnGlobalLayoutListener { channel.trySend(this@globalLayouts) }
    viewTreeObserver.addOnGlobalLayoutListener(listener)
    awaitClose {
        viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }
}

fun View.safeAddOnGlobalFocusListener(listener: OnGlobalFocusChangeListener): AutoCloseable {
    return DetachingViewListener(
        this,
        add = { it.viewTreeObserver.addOnGlobalFocusChangeListener(listener) },
        remove = { it.viewTreeObserver.removeOnGlobalFocusChangeListener(listener) },
    ).init()
}

@Suppress("FunctionMaxLength")
fun View.safeSetOnApplyWindowInsetsListener(listener: OnApplyWindowInsetsListener): AutoCloseable {
    return DetachingViewListener(
        this,
        add = { ViewCompat.setOnApplyWindowInsetsListener(it, listener) },
        remove = { ViewCompat.setOnApplyWindowInsetsListener(it, null) },
    ).init()
}

@Suppress("FunctionMaxLength")
fun View.calculationCoordinatesRelativeToView(anotherView: ViewGroup): Rect {
    val offsetViewBounds = Rect()
    this.getDrawingRect(offsetViewBounds)
    anotherView.offsetDescendantRectToMyCoords(this, offsetViewBounds)
    return offsetViewBounds
}

fun AppBarLayout.safeAddOnOffsetChangedListener(listener: AppBarLayout.OnOffsetChangedListener): AutoCloseable {
    return DetachingViewListener(
        this,
        add = { (it as AppBarLayout).addOnOffsetChangedListener(listener) },
        remove = { (it as AppBarLayout).removeOnOffsetChangedListener(listener) }
    ).init()
}

fun RecyclerView.safeAddOnScrollListener(listener: RecyclerView.OnScrollListener): AutoCloseable {
    return DetachingViewListener(
        this,
        add = { (it as RecyclerView).addOnScrollListener(listener) },
        remove = { (it as RecyclerView).removeOnScrollListener(listener) }
    ).init()
}

private class DetachingViewListener(
    private val view: View,
    private val add: (View) -> Unit,
    private val remove: (View) -> Unit,
) : View.OnAttachStateChangeListener {

    private var isAdded = false

    fun init(): AutoCloseable {
        view.addOnAttachStateChangeListener(this)
        add()
        return AutoCloseable { dispose() }
    }

    private fun dispose() {
        remove()
        view.removeOnAttachStateChangeListener(this)
    }

    private fun add() {
        if (!isAdded) {
            add(view)
            isAdded = true
        }
    }

    private fun remove() {
        remove(view)
        isAdded = false
    }

    override fun onViewAttachedToWindow(p0: View) = add()

    override fun onViewDetachedFromWindow(p0: View) = remove()
}
