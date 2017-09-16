package jp.cordea.xx

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import io.reactivex.BackpressureStrategy
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), HandlerBase.OnOutputLogListener {

    private val handler = SourceHandler().also {
        it.setOnOutputLogListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        spinner.adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                Source.values().map { it.toString() })

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>) {
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val source = Source
                        .values()[parent.selectedItemPosition]
                backpressureSpinner.visibility = if (source == Source.FLOWABLE) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }

        backpressureSpinner.adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                BackpressureStrategy.values().map {
                    it.toString().toLowerCase().capitalize()
                })

        button.setOnClickListener {
            val source = Source
                    .values()[spinner.selectedItemPosition]
            val backpressure = BackpressureStrategy
                    .values()[backpressureSpinner.selectedItemPosition]
            textView.text = ""
            handler.handle(source, backpressure)
        }
    }

    override fun onPause() {
        super.onPause()
        handler.dispose()
    }

    override fun onOutput(log: String) {
        runOnUiThread {
            textView.text = "%s\n%s".format(textView.text, log)
        }
    }
}
