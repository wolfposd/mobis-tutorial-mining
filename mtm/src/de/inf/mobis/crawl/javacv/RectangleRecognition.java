package de.inf.mobis.crawl.javacv;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import java.awt.Color;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import de.inf.mobis.crawl.analyze.Box;

public class RectangleRecognition
{

    private final int COLORDISTANCETORED;

    public final Color COLORTOCOMPARE;

    private final Color _background;

    private int BACKGROUNDCOLORDISTANCE;

    public RectangleRecognition(int distance, int backgroundDistance, Color color, Color background)
    {
        COLORDISTANCETORED = distance;
        BACKGROUNDCOLORDISTANCE = backgroundDistance;
        COLORTOCOMPARE = color;
        _background = background;
    }

    public RectangleRecognition()
    {
        this(100, 100, Color.RED, Color.RED);
    }

    public int scanImage(String filename)
    {
        return scanImage(filename, false, COLORDISTANCETORED, BACKGROUNDCOLORDISTANCE);
    }

    public int scanImage(String filename, boolean showAreas, int colorDistance, int backgroundDistance)
    {
        final IplImage colored = cvLoadImage(filename);

        if (colored == null || !colored.getClass().equals(IplImage.class))
        {
            System.err.println("Not working:" + filename);
            return 0;
        }

        RGBColor mostCommonColor = RGBColor.get(cvAvg(colored, null));
        if (distanceToCOLOR(mostCommonColor, _background) < backgroundDistance)
        {
            return 0;
        }

        IplImage gray = cvCreateImage(cvGetSize(colored), IPL_DEPTH_8U, 1);

        cvCvtColor(colored, gray, CV_BGR2GRAY);

        // cvThreshold(gray, gray, 60, 255, CV_THRESH_BINARY);

        final int mechanism = CV_ADAPTIVE_THRESH_GAUSSIAN_C;
        final int blocks = 3;

        cvAdaptiveThreshold(gray, gray, 60, mechanism, CV_THRESH_BINARY, blocks, 0);

        cvCanny(gray, gray, 0, 50, 3);

        CvMemStorage storage = CvMemStorage.create();
        CvSeq contours = new CvSeq();

        cvFindContours(gray, storage, contours, Loader.sizeof(CvContour.class), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);

        HashSet<Box> boxes = new HashSet<>();

        CvSeq ptr = new CvSeq();
        ptr = contours;
        while (ptr != null && !ptr.isNull())
        {
            CvRect boundbox = cvBoundingRect(ptr, 0);

            int width = boundbox.width();
            int height = boundbox.height();

            if (width >= 20 && height >= 20)
            {
                int x = boundbox.x();
                int y = boundbox.y();
                int x2 = x + width;
                int y2 = y + height;

                RGBColor color1 = RGBColor.get(cvGet2D(colored, y, x));
                RGBColor color2 = RGBColor.get(cvGet2D(colored, y2, x));
                RGBColor color3 = RGBColor.get(cvGet2D(colored, y, x2));
                RGBColor color4 = RGBColor.get(cvGet2D(colored, y2, x2));

                if (distanceToCOLOR(color1) < colorDistance && distanceToCOLOR(color2) < colorDistance
                        && distanceToCOLOR(color3) < colorDistance && distanceToCOLOR(color4) < colorDistance)
                {
                    Box b = Box.get(x, y, x2, y2);
                    boxes.add(b);
                }
            }
            ptr = ptr.h_next();
        }

        if (showAreas && !boxes.isEmpty())
        {
            IplImage resultImage = cvCloneImage(colored);

            for (Box b : boxes)
            {
                cvRectangle(resultImage, cvPoint(b.x1, b.y1), cvPoint(b.x2, b.y2), cvScalar(0, 255, 0, 0), 3, 0, 0);
            }
            // showImg(gray);
            showImg(resultImage);
        }

        return boxes.size();
    }

    public static void harrisCorners(IplImage gray)
    {
        IplImage harris = IplImage.create(cvGetSize(gray), IPL_DEPTH_32F, 1);
        cvCornerHarris(gray, harris, 3, 3, 0.01);

        IplImage harrisCorners = IplImage.create(cvGetSize(gray), IPL_DEPTH_8U, 1);
        double threshold = 0.0001;
        cvThreshold(harris, harrisCorners, threshold, 255, CV_THRESH_BINARY_INV);

        showImg(harrisCorners);
    }

    public static void showImg(IplImage image)
    {
        ImageIcon icon = new ImageIcon(image.getBufferedImage());
        JFrame frame = new JFrame();
        JLabel label = new JLabel(icon);
        frame.add(label);
        frame.setVisible(true);
        frame.setSize(image.width(), image.height());
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public int distanceToCOLOR(RGBColor c, Color color)
    {
        double red = Math.pow(c.R - color.getRed(), 2);
        double blue = Math.pow(c.B - color.getBlue(), 2);
        double green = Math.pow(c.G - color.getGreen(), 2);
        return (int) Math.sqrt(red + blue + green);
    }

    public int distanceToCOLOR(RGBColor c)
    {
        return distanceToCOLOR(c, COLORTOCOMPARE);
    }

    public static class RGBColor
    {
        int R;
        int G;
        int B;

        @Override
        public String toString()
        {
            return "RGBColor [R=" + R + ", G=" + G + ", B=" + B + "]";
        }

        public static RGBColor get(CvScalar sc)
        {
            RGBColor r = new RGBColor();

            r.R = (int) sc.val(2);
            r.G = (int) sc.val(1);
            r.B = (int) sc.val(0);
            return r;
        }
    }

}
