package com.mikolove.allmightworkout.framework.presentation.main.exercise_detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.core.domain.exercise.ExerciseType
import com.mikolove.allmightworkout.business.domain.model.WorkoutType
import com.mikolove.allmightworkout.util.printLogD

class WorkoutTypeAdapter(
    private val mContext: Context,
    private val layoutResource : Int,
    private var workoutTypes : List<WorkoutType>
) : ArrayAdapter<WorkoutType>(mContext,layoutResource,workoutTypes){

    fun getItems() : List<WorkoutType>{
        return workoutTypes
    }

    override fun getCount(): Int {
        return workoutTypes.size
    }
    override fun getItem(position: Int):WorkoutType {
        return workoutTypes[position]
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertedView = convertView
        if (convertedView == null) {
            val inflater = LayoutInflater.from(mContext)
            convertedView = inflater.inflate(layoutResource, parent, false)
        }
        try {
            val workoutType : WorkoutType = getItem(position)
            val workoutTypeAutoCompleteView = convertedView!!.findViewById<View>(R.id.autoCompleteTextWorkoutType) as TextView
            workoutTypeAutoCompleteView.text = workoutType.name.replaceFirstChar { it.uppercaseChar() }
        } catch (e: Exception) {
            printLogD("WorkoutTypeAdapter","Error : ${e}")
        }
        return convertedView!!
    }

    fun submitList(workoutTypes : List<WorkoutType>){
        clear()
        addAll(workoutTypes)
        this.workoutTypes = workoutTypes
        notifyDataSetChanged()
    }
}

class BodyPartAdapter(
    private val mContext: Context,
    private val layoutResource : Int,
    private var bodyParts : List<BodyPart>
) : ArrayAdapter<BodyPart>(mContext,layoutResource,bodyParts) {

    fun getItems(): List<BodyPart> {
        return bodyParts
    }

    override fun getCount(): Int {
        return bodyParts.size
    }

    override fun getItem(position: Int): BodyPart {
        return bodyParts[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertedView = convertView
        if (convertedView == null) {
            convertedView = LayoutInflater.from(mContext).inflate(layoutResource, parent, false)
        }
        try {
            val bodyPart: BodyPart = getItem(position)
            val bodyPartAutoCompleteView =
                convertedView!!.findViewById<View>(R.id.autoCompleteTextBodyPart) as TextView
            bodyPartAutoCompleteView.text = bodyPart.name.replaceFirstChar { it.uppercaseChar() }
        } catch (e: Exception) {
            printLogD("BodyPartAdapter", "Error : ${e}")
        }
        return convertedView!!
    }

    fun submitList(bodyParts: List<BodyPart>) {
        clear()
        addAll(bodyParts)
        this.bodyParts = bodyParts
        notifyDataSetChanged()
    }
}

class ExerciseTypeAdapter(
    private val mContext: Context,
    private val layoutResource : Int,
    private var exerciseTypes : List<ExerciseType>
) : ArrayAdapter<ExerciseType>(mContext,layoutResource,exerciseTypes){

    fun getItems() : List<ExerciseType>{
        return exerciseTypes
    }

    override fun getCount(): Int {
        return exerciseTypes.size
    }
    override fun getItem(position: Int): ExerciseType {
        return exerciseTypes[position]
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var converedtView = convertView
        if (converedtView == null) {
            converedtView = LayoutInflater.from(mContext).inflate(layoutResource, parent, false)
        }
        try {
            val exerciseType : ExerciseType = getItem(position)
            val exerciseTypeAutoCompleteView = converedtView!!.findViewById<View>(R.id.autoCompleteTextExerciseType) as TextView
            exerciseTypeAutoCompleteView.text = exerciseType.type.replaceFirstChar { it.uppercaseChar() }
        } catch (e: Exception) {
            printLogD("ExerciseTypeAdapter","Error : ${e}")
        }
        return converedtView!!
    }

    fun submitList(exerciseTypes : List<ExerciseType>){
        clear()
        addAll(exerciseTypes)
        this.exerciseTypes = exerciseTypes
        notifyDataSetChanged()
    }
}