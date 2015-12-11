package aoak.projects.hobby.dsp.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.math3.complex.Complex;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * operations on signals are easier to verify and test if it is possible to see the plots.
 * This utility class will provide methods to plot or save plots of signals. It provides
 * a wrapper around JFreeChart library.
 * @author aniket
 *
 */
public class PlottingUtils {

    /**
     * Plot real part of values in array on Y axis and index on X axis. Save to a jpeg file of the
     * name specified in frameName
     * @param frameName
     * @param input
     * @throws IOException
     */
    public static void savePlot(Complex[] input, String frameName) throws IOException {
        DefaultCategoryDataset inputSet = new DefaultCategoryDataset();
        for (int i=0; i < input.length; i++) {
            inputSet.addValue(input[i].getReal(), "amplitude", String.valueOf(i));
        }
        JFreeChart linechart = ChartFactory.createLineChart(frameName, "time", "amplitude", inputSet, PlotOrientation.VERTICAL, true, true, false);
        File c = new File(frameName + ".jpeg");
        ChartUtilities.saveChartAsJPEG(c, linechart, 640, 480);
    }

    /**
     * Plot values in array on Y axis and index on X axis. Save to a jpeg file of the
     * name specified in frameName
     * @param input
     * @param frameName
     * @throws IOException
     */
    public static void savePlot(Number[] input, String frameName) throws IOException {
        DefaultCategoryDataset inputSet = new DefaultCategoryDataset();
        for (int i=0; i < input.length; i++) {
            inputSet.addValue(input[i], "amplitude", String.valueOf(i));
        }
        JFreeChart linechart = ChartFactory.createLineChart(frameName, "time", "amplitude", inputSet, PlotOrientation.VERTICAL, true, true, false);
        File c = new File(frameName + ".jpeg");
        ChartUtilities.saveChartAsJPEG(c, linechart, 640, 480);
    }

    /**
     * Plot real part of values in array on Y axis and index on X axis. Save to a jpeg file of the
     * name specified in frameName. Subsamples the signal according to subSamplingRate parameter
     * @param input
     * @param frameName
     * @param subSamplingRate
     * @throws IOException
     */
    public static void savePlot(Complex[] input, String frameName, int subSamplingRate) throws IOException {
        DefaultCategoryDataset inputSet = new DefaultCategoryDataset();
        for (int i=0; i < input.length/subSamplingRate; i++) {
            inputSet.addValue(input[i*subSamplingRate].getReal(), "amplitude", String.valueOf(i));
        }
        JFreeChart linechart = ChartFactory.createLineChart(frameName, "time", "amplitude", inputSet, PlotOrientation.VERTICAL, true, true, false);
        File c = new File(frameName + ".jpeg");
        ChartUtilities.saveChartAsJPEG(c, linechart, 640, 480);
    }
}
