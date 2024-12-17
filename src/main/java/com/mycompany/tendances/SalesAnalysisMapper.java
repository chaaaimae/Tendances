package com.mycompany.tendances;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SalesAnalysisMapper extends Mapper<Object, Text, Text, DoubleWritable> {
    private final static DoubleWritable salesAmount = new DoubleWritable();

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // Ignorer l'en-tête
        if (key instanceof LongWritable && ((LongWritable) key).get() == 0) {
            return;
        }

        // Séparer la ligne CSV
        String[] fields = value.toString().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        // Extraire les champs nécessaires
        String product = fields[1];  // Colonne 'Product'
        String region = fields[7];  // Colonne 'Region'
        String month = fields[6];  // Colonne 'Month'

        // Quantité commandée
        double quantity = Double.parseDouble(fields[2]);

        // Générer la clé combinée : produit + région + mois
        String keyOutput = product + "_" + region + "_" + month;

        // Définir la quantité de vente
        salesAmount.set(quantity);  // Utiliser la quantité commandée

        // Écrire la clé (produit, région, mois) et la valeur (quantité)
        context.write(new Text(keyOutput), salesAmount);
    }
}
