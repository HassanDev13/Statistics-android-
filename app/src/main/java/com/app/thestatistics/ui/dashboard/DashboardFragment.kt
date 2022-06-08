package com.app.thestatistics.ui.dashboard

import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.thestatistics.R
import com.app.thestatistics.databinding.FragmentDashboardBinding
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils
import com.google.firebase.firestore.FirebaseFirestore


class DashboardFragment : Fragment(), OnChartValueSelectedListener {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    val sizes: MutableList<Int> = ArrayList()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        var size1: Int = 0
        var size2: Int = 0
        val db = FirebaseFirestore.getInstance()
        db.collection("Courses")
            //.whereArrayContains("name","hassan")
            .whereEqualTo("name", "hassan")
            .get()
            .addOnSuccessListener { documents ->
                // val courses = documents.toObject<coursesModule>()
                size1 = documents.size()
                sizes.add(size1)
                Log.d("getData", "size ${size1}")


            }.addOnFailureListener { exception ->
                Log.w("getData", "Error getting documents: ", exception)
            }


        db.collection("Courses")
            //.whereArrayContains("name","hassan")
            .whereEqualTo("name", "aa")
            .get()
            .addOnSuccessListener { documents ->
                // val courses = documents.toObject<coursesModule>()
                size2 = documents.size()
                sizes.add(size2)
                Log.w("size", "Error getting documents: ${sizes.size}")

                try {
                    setData(sizes)
                } catch (e: Exception) {
                    Log.w("getDataError", "Error getting documents: ", e)

                }
                Log.d("getData", "size ${size2}")

            }.addOnFailureListener { exception ->
                Log.w("getData", "Error getting documents: ", exception)
            }

        binding.PieChart.setUsePercentValues(true);
        binding.PieChart.getDescription()?.setEnabled(false);
        binding.PieChart.setExtraOffsets(5F, 10F, 5F, 5F);

        binding.PieChart.setDragDecelerationFrictionCoef(0.95f);

        //chart.setCenterTextTypeface("tfLight");
        //chart?.centerText = generateCenterSpannableText();

        binding.PieChart.setDrawHoleEnabled(true);
        binding.PieChart.setHoleColor(Color.WHITE);

        binding.PieChart.setTransparentCircleColor(Color.WHITE);
        binding.PieChart.setTransparentCircleAlpha(110);

        binding.PieChart.setHoleRadius(58f);
        binding.PieChart.setTransparentCircleRadius(61f);

        binding.PieChart.setDrawCenterText(true);

        binding.PieChart.setRotationAngle(0F);
        try{
            lineChart()
        }catch (exception:Exception){
            Log.w("lineChart", "Error getting documents: ", exception)

        }

        val root: View = binding.root


        return root
    }

    fun setData(ranges: MutableList<Int>) {
        val entries: ArrayList<PieEntry> = ArrayList()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (range in ranges) {
            Log.d("getData", "range[i].toFloat() ${range.toFloat()}")
            entries.add(
                PieEntry(
                    range.toFloat(),
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_home_black_24dp
                    ),
                    range.toFloat(),
                )
            )
        }
        val dataSet = PieDataSet(entries, "Election Results")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0F, 40F)
        dataSet.selectionShift = 5f

        // add a lot of colors
        val colors: ArrayList<Int> = ArrayList()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)

        binding.PieChart.data = data

        // undo all highlights
        binding.PieChart.highlightValues(null)
        binding.PieChart.invalidate()

    }


    fun lineChart() {
        val chart = binding.BarChart
        chart.setBackgroundColor(Color.WHITE)

        // disable description text

        // disable description text
        chart.getDescription().setEnabled(false)

        // enable touch gestures

        // enable touch gestures
        chart.setTouchEnabled(false)

        // set listeners

        // set listeners
        chart.setOnChartValueSelectedListener(this)
        chart.setDrawGridBackground(false)

        // create marker to display box when values are selected

        // create marker to display box when values are selected
        // val mv = MyMarkerView(this, R.layout.custom_marker_view)

        // Set the marker to the chart

        // Set the marker to the chart
        //  mv.setChartView(chart)
        // chart.setMarker(mv)

        // enable scaling and dragging

        // enable scaling and dragging
        chart.setDragEnabled(true)
        chart.setScaleEnabled(true)
        // chart.setScaleXEnabled(true);
        // chart.setScaleYEnabled(true);

        // force pinch zoom along both axis
        // chart.setScaleXEnabled(true);
        // chart.setScaleYEnabled(true);

        // force pinch zoom along both axis
        chart.setPinchZoom(true)

        var xAxis: XAxis

        xAxis = chart.xAxis

        // vertical grid lines
        xAxis.enableGridDashedLine(10f, 10f, 0f)

        var yAxis: YAxis

        yAxis = chart.axisLeft

        // disable dual axis (only use LEFT axis)
        chart.axisRight.isEnabled = false

        // horizontal grid lines
        yAxis.enableGridDashedLine(10f, 10f, 0f)

        // axis range
        yAxis.axisMaximum = 200f
        yAxis.axisMinimum = 0f


        val llXAxis = LimitLine(9f, "Index 10")
        llXAxis.lineWidth = 4f
        llXAxis.enableDashedLine(10f, 10f, 0f)
        llXAxis.labelPosition = LimitLabelPosition.RIGHT_BOTTOM
        llXAxis.textSize = 10f
        val ll1 = LimitLine(150f, "Upper Limit")
        ll1.lineWidth = 4f
        ll1.enableDashedLine(10f, 10f, 0f)
        ll1.labelPosition = LimitLabelPosition.RIGHT_TOP
        ll1.textSize = 10f
        val ll2 = LimitLine(-30f, "Lower Limit")
        ll2.lineWidth = 4f
        ll2.enableDashedLine(10f, 10f, 0f)
        ll2.labelPosition = LimitLabelPosition.RIGHT_BOTTOM
        ll2.textSize = 10f

        // draw limit lines behind data instead of on top
        yAxis.setDrawLimitLinesBehindData(true)
        xAxis.setDrawLimitLinesBehindData(true)

        // add limit lines
        yAxis.addLimitLine(ll1)
        yAxis.addLimitLine(ll2)
        //xAxis.addLimitLine(llXAxis);
        // add data
        // add data

        setDataBar(45, 180F)

        // draw points over time

        // draw points over time
        chart.animateX(1500)

        // get the legend (only possible after setting data)

        // get the legend (only possible after setting data)
        val l = chart.legend

        // draw legend entries as lines

        // draw legend entries as lines
        l.form = LegendForm.LINE
    }
    fun setDataBar(count:Int, range:Float ){
        val values: ArrayList<Entry> = ArrayList()

        for (i in 0 until count) {
            val data = (Math.random() * range).toFloat() - 30
            values.add(Entry(i.toFloat(), data, resources.getDrawable(R.drawable.ic_home_black_24dp)))
        }
        val set1: LineDataSet
        val chart = binding.BarChart
        if (chart.getData() != null &&
            chart.getData().getDataSetCount() > 0
        ) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            set1.notifyDataSetChanged()
            chart.getData().notifyDataChanged()
            chart.notifyDataSetChanged()
        }else{
            // create a dataset and give it a type
            // create a dataset and give it a type
            set1 = LineDataSet(values, "DataSet 1")

            set1.setDrawIcons(false)

            // draw dashed line

            // draw dashed line
            set1.enableDashedLine(10f, 5f, 0f)

            // black lines and points

            // black lines and points
            set1.color = Color.GREEN
            set1.setCircleColor(Color.RED)

            // line thickness and point size

            // line thickness and point size
            set1.lineWidth = 1f
            set1.circleRadius = 3f
            // draw points as solid circles
            // draw points as solid circles
            set1.setDrawCircleHole(false)

            // customize legend entry

            // customize legend entry
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f

            // text size of values

            // text size of values
            set1.valueTextSize = 9f

            // draw selection line as dashed

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f)
            // set the filled area
            // set the filled area
            set1.setDrawFilled(true)
            set1.fillFormatter =
                IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }

            // set color of filled area

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_home_black_24dp)
                set1.fillDrawable = drawable
            } else {
                set1.fillColor = Color.GREEN
            }
            val dataSets: ArrayList<ILineDataSet> = ArrayList()
            dataSets.add(set1) // add the data sets


            // create a data object with the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)

            // set data

            // set data
            chart.setData(data)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected() {
        TODO("Not yet implemented")
    }
}