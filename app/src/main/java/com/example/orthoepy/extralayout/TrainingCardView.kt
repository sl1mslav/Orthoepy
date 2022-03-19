package com.example.orthoepy.extralayout

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.orthoepy.R
import com.example.orthoepy.databinding.TrainingCardBinding

class TrainingCardView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: TrainingCardBinding

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.training_card, this, true)
        binding = TrainingCardBinding.bind(this)
        initAttributes(attrs, defStyleAttr)
    }

    private fun initAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TrainingCardView)

        val labelText = typedArray.getString(R.styleable.TrainingCardView_labelText)
        binding.labelText.text = labelText ?: "*название_тренировки*"

        val descriptionText = typedArray.getString(R.styleable.TrainingCardView_descriptionText)
        binding.descriptionText.text = descriptionText ?: "*описание*"

        val icon = typedArray.getDrawable(R.styleable.TrainingCardView_icon)
        binding.icon.setImageDrawable(icon)

        typedArray.recycle()
    }

}