package android.tyit.com.asyncthreads

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.AsyncTask
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    var btn: Button? = null
    var statusText: TextView? = null
    var statusProgress: ProgressBar? = null
    var stringBuilder: StringBuilder? = null
    var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn = findViewById(R.id.btn_async);
        statusText = findViewById(R.id.statusText)
        statusText?.movementMethod = ScrollingMovementMethod()
        statusProgress = findViewById(R.id.progressBar)
        progressBar?.max = 2;
        progressBar?.progress = 0
        stringBuilder = StringBuilder("Priyal 92\nAsync task Demonstration\n---------------------")
        stringBuilder?.append("\nWaiting for Async Jobs to start\n")
        statusText?.text = "${stringBuilder.toString()}"

        btn?.setOnClickListener(View.OnClickListener {
            var task: MyAsyncTask = MyAsyncTask()
            task.execute("www.xyz.com", "www.abc.com")
        })

    }

    // Create inner class by extending the AsyncTask
    inner class MyAsyncTask : AsyncTask<String, Int, Int>() {
        //Override the doInBackground method
        override fun doInBackground(vararg params: String?): Int {
            val count: Int = params.size
            var index = 0
            while (index < count) {
                Log.d("Kotlin", "doInBackground task and Total parameter passed are :$count " +
                        "and processing $index with value: ${params[index]}")
                // Publish the progress
                publishProgress(index + 1)
                //Sleeping for 1 seconds
                Thread.sleep(1000)
                index++
            }
            return count;
        }

        // Override the onProgressUpdate method to post the update on main thread
        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            if (values[0] != null) {
                statusProgress?.progress = values[0] as Int
                stringBuilder?.append("Published post called with ${values[0]}\n")
                statusText?.text = stringBuilder.toString()
            }
        }

        // Setup the intial UI before execution of the background task
        override fun onPreExecute() {
            super.onPreExecute()
            stringBuilder?.append("Async task started... \n In PreExecute Method \n")
            statusText?.text = "${stringBuilder.toString()}"
            progressBar?.visibility = View.VISIBLE
            progressBar?.progress = 0
            Log.d("Kotlin", "On PreExecute Method")
        }

        // Update the final status by overriding the OnPostExecute method.
        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)
            stringBuilder?.append("Task Completed.\n")
            statusText?.text = "${stringBuilder.toString()}"
            Log.d("Kotlin", "On Post Execute and size of String is:$result")
        }
    }

}
