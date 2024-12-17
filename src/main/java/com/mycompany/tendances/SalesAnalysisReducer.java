package com.mycompany.tendances;

import java.io.IOException;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SalesAnalysisReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {
    private final static DoubleWritable result = new DoubleWritable();

    @Override
    public void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
        double totalSales = 0;

        // Additionner les quantités de toutes les ventes pour la même clé (produit, région, mois)
        for (DoubleWritable val : values) {
            totalSales += val.get();  // Additionner les quantités
        }

        result.set(totalSales);  // Définir la somme totale des quantités

        // Écrire la clé (produit, région, mois) et la somme des quantités
        context.write(key, result);
    }
}

