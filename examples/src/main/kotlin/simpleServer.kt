import hep.dataforge.meta.invoke
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.style
import scientifik.plotly.Plot2D
import scientifik.plotly.Plotly
import scientifik.plotly.models.Trace
import scientifik.plotly.models.invoke
import scientifik.plotly.server.serve
import scientifik.plotly.staticPlot
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@KtorExperimentalAPI
@ExperimentalCoroutinesApi
fun main() {
    val server = Plotly.serve {
        val x = (0..100).map { it.toDouble() / 100.0 }.toDoubleArray()
        val y1 = x.map { sin(2.0 * PI * it) }.toDoubleArray()
        val y2 = x.map { cos(2.0 * PI * it) }.toDoubleArray()

        val trace1 = Trace.invoke(x, y1) { name = "sin" }
        val trace2 = Trace.invoke(x, y2) { name = "cos" }

        lateinit var plot1: Plot2D

        //root level plots go to default page
        page {
            h1 { +"This is the plot page" }
            a("/other") { +"The other page" }
            div {
                style = "display: flex;   align-items: stretch; "
                div {
                    style = "width: 64%;"
                    plot1 = staticPlot {
                        traces(trace1, trace2)
                        layout {
                            title = "First graph, row: 1, size: 8/12"
                            xaxis { title = "x axis name" }
                            yaxis { title = "y axis name" }
                        }
                    }
                }
                div {
                    style = "width: 32%;"
                    staticPlot {
                        traces(trace1, trace2)
                        layout {
                            title = "Second graph, row: 1, size: 4/12"
                            xaxis { title = "x axis name" }
                            yaxis { title = "y axis name" }
                        }
                    }
                }
            }



            div {
                staticPlot {

                    traces(trace1, trace2)
                    layout {
                        title = "Third graph, row: 2, size: 12/12"
                        xaxis { title = "x axis name" }
                        yaxis { title = "y axis name" }
                    }
                }
            }
        }

        page("other") {
            h1 { +"This is the other plot page" }
            a("/") { +"Back to the main page" }
            staticPlot(plot1)
        }

    }

    server.show()

    println("Press Enter to close server")
    readLine()

    server.close()

}