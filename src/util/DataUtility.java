package util;
import Model.Messwert;

import java.util.Date;
import java.util.TreeMap;

public class DataUtility {
    /*
    public static TreeMap<Long,Messwert> convertAbsoluteValueToRelative(TreeMap<Long, Messwert> absoluteValues){
        TreeMap<Long, Messwert> relativeValues = absoluteValues; //map which will be returned
        double temp = 0; //set previous value to 0, so that the first relative value is equal to the absolute
        for (Long date : absoluteValues.keySet()) {
            relativeValues.get(date).getAbsoluterWert(absoluteValues.get(date).getValue()-temp); //calculate the relative using the previous value
            temp = absoluteValues.get(date).getAbsoluterWert(); //set the previous value
        }
        return relativeValues;
    }

   public static TreeMap<Long, Messwert> getChangedValue(TreeMap<Long, Messwert> absoluteValues, TreeMap<Long, Messwert> relativeValues){
        TreeMap<Long, Messwert> changedValues = new TreeMap<>(); //map which will be returned
        double temp = 0;
        for (Long date : relativeValues.keySet()) { //iterate through map with relative values
            if (absoluteValues.containsKey(date)){ //check if absolute and relative values have a common key
                temp = absoluteValues.get(date).getValue(); //set temp to be the value with the common key of relative and absolute
                changedValues.put(date, absoluteValues.get(date)); //set the changed value to be the absolute value
                continue; //skips setting the changed value to relative + absolute value
            }
            changedValues.get(date).setAbosluterWert(temp+relativeValues.get(date).getAbsoluterWert()); //set changed value to be relative + absolute value
        }
        return changedValues;
    } */
}
