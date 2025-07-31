package main;

import browser.NgordnetQuery;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.List;

import browser.NgordnetQueryHandler;


public class HistoryTextHandler extends NgordnetQueryHandler{

    public NGramMap ngm;

    public HistoryTextHandler(NGramMap map){
        ngm = map;
    }


@Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        String response = "";

        for (int i = 0; i <words.size(); i += 1){
            String word = words.get(i);
            response += word + ": {";
            TimeSeries t = ngm.weightHistory(word, startYear, endYear);
            if (t.isEmpty()){
                response += "invalid word}\n";
                continue;
            }
            List<Integer> years = t.years();
            for (int j = 0; j < years.size() - 1; j += 1){
                String year = years.get(i).toString();
                String number = t.get(years.get(i)).toString();
                response += year + "=" + number + ", ";
            }
            String year = years.get(years.size()-1).toString();
            String number = t.get(years.get(years.size()-1)).toString();
            response += year + "=" + number;
            response += "}\n";
        }
        return response;
    }
}
