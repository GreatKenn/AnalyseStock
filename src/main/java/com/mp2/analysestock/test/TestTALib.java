package com.mp2.analysestock.test;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

public class TestTALib {

    private static double bias(double ma_value, double close_value) {
        double re = 0;

        if (ma_value > 0 && close_value > 0) {
            re = (close_value - ma_value) / ma_value * 100;
            re = (double) Math.round(re * 100) / 100;
        }

        return re;
    }

    public static void main(String[] args) {
        int PERIODS_AVERAGE = 10;
        double[] closePrice = {2716.5104,
                2721.013,
                2725.8367,
                2583.4575,
                2606.9125,
                2568.0984,
                2546.3296,
                2561.614,
                2486.4186,
                2550.4652,
                2654.8762,
                2594.8255,
                2603.2951,
                2603.7995,
                2598.8468,
                2542.1033,
                2568.0481,
                2602.7832,
                2606.2372,
                2676.4762,
                2665.4306,
                2659.3564,
                2641.342,
                2635.6322,
                2598.8715,
                2630.5195,
                2654.8795,
                2632.2425,
                2668.1704,
                2679.1097,
                2703.5116,
                2645.8545,
                2651.5053,
                2645.4339,
                2579.4831,
                2575.8101,
                2574.6792,
                2601.7365,
                2567.4434,
                2588.1875,
                2654.798,
                2665.9577,
                2649.8051,
                2605.1813,
                2605.8876,
                2584.5822,
                2594.0881,
                2602.1526,
                2634.0491,
                2593.7407};
        double[] out = new double[closePrice.length];
        double[] outMACD = new double[closePrice.length];
        double[] outMACDSignal = new double[closePrice.length];
        double[] outMACDHist = new double[closePrice.length];
        Core taLibCore = new Core();
        MInteger begin = new MInteger();
        MInteger length = new MInteger();

        RetCode retCodeEMA = taLibCore.ema(0, closePrice.length - 1, closePrice, PERIODS_AVERAGE, begin, length, out);

        if (retCodeEMA == RetCode.Success) {
            System.out.println("Output Start Period: " + begin.value);
            System.out.println("Output End Period: " + (begin.value + length.value - 1));

            for (int i = begin.value; i < begin.value + length.value; i++) {
                StringBuilder line = new StringBuilder();
                line.append("Period #");
                line.append(i);
                line.append(" close=");
                line.append(closePrice[i]);
                line.append(" mov_avg=");
                line.append(out[i - begin.value]);
                line.append(" bias=" + bias(out[i - begin.value], closePrice[i]));
                System.out.println(line.toString());
            }
        } else {
            System.out.println("Error");
        }

        System.out.println("============================================");

        RetCode retCodeMACD = taLibCore.macd(0, closePrice.length - 1, closePrice, 12, 26, 9, begin, length, outMACD, outMACDSignal, outMACDHist);

        if (retCodeMACD == RetCode.Success) {
            System.out.println("Output Start Period: " + begin.value);
            System.out.println("Output Length Period: " + length.value);

            for (int i = 0; i < closePrice.length; i++) {
                StringBuilder line = new StringBuilder();
                line.append("Period #");
                line.append(i);
                line.append(" close=");
                line.append(closePrice[i]);
                line.append(" MACD=");
                line.append(outMACD[i]);
                line.append(" MACDSignal=");
                line.append(outMACDSignal[i]);
                line.append(" MACDHist=");
                line.append(outMACDHist[i]);
                System.out.println(line.toString());
            }
        } else {
            System.out.println("Error");
        }
    }
}
