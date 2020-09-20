package com.mrtvrgn.gameanalysis.screen.basketball

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.mrtvrgn.gameanalysis.R
import com.mrtvrgn.gameanalysis.model.HexTileGraphics
import kotlinx.android.synthetic.main.activity_heatmap_records.court_container as courtContainer
import kotlinx.android.synthetic.main.activity_heatmap_records.players_dropdown as playersDropdown
import kotlinx.android.synthetic.main.activity_heatmap_records.rate_progress as rateProgress
import kotlinx.android.synthetic.main.activity_heatmap_records.rate_text as rateText


class BasketballHeatMapActivity : AppCompatActivity(), HeatMapRecordsView {

    private val presenter = HeatMapRecordsPresenter(BasketballHeatMapModel())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_heatmap_records)

        courtContainer.setImageResource(
            resources.getIdentifier(
                "basketballcourt",
                "raw",
                packageName
            )
        )
        presenter.attachView(this)
    }

    override fun onStart() {
        super.onStart()

        courtContainer.post {
            presenter.generateMap(
                courtContainer.left.toFloat(),
                courtContainer.top.toFloat(),
                courtContainer.right.toFloat(),
                courtContainer.bottom.toFloat()
            )
        }
    }

    override fun drawMap(tiles: List<HexTileGraphics>) {
        courtContainer.drawTiles(tiles)
    }

    override fun addPlayersData(data: List<String>) {
        playersDropdown.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data)
    }

    override fun setSelectedPlayerPosition(index: Int) {
        playersDropdown.setSelection(index)
    }

    override fun setOnPlayersDropdownItemSelectedBehavior(behavior: (selectedIndex: Int) -> Unit) {
        playersDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(a: AdapterView<*>?, v: View?, position: Int, id: Long) {
                behavior.invoke(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
    }

    override fun setSuccessRate(progress: Int, rate: String) {
        rateProgress.progress = progress
        rateText.text = rate
    }
}