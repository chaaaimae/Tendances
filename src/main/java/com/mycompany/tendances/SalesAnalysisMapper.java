package com.mycompany.tendances;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SalesAnalysisMapper extends Mapper<Object, Text, Text, IntWritable> {
    private final static IntWritable salesQuantity = new IntWritable();

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // Ignorer l'en-tête
        if (key instanceof LongWritable && ((LongWritable) key).get() == 0) {
            return;
        }

        // Séparer la ligne CSV
        String[] fields = value.toString().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        // Vérifier si toutes les colonnes nécessaires sont présentes
        if (fields.length < 8) {
            return; // Ligne invalide
        }

        try {
            // Extraire les champs nécessaires
            String product = fields[1];  // Colonne 'Product'
            String region = fields[7];   // Colonne 'Region'
            String month = fields[6];    // Colonne 'Month'

            // Quantité commandée
            int quantity = (int) Double.parseDouble(fields[2]);

            // Générer la clé combinée : produit + région + mois
            String keyOutput = product + "_" + region + "_" + month;

            // Définir la quantité de vente
            salesQuantity.set(quantity);

            // Écrire la clé (produit, région, mois) et la valeur (quantité)
            context.write(new Text(keyOutput), salesQuantity);
        } catch (NumberFormatException e) {
            // Gérer les lignes mal formées
            System.err.println("Erreur de parsing : " + value.toString());
        }
    }
}
