package util;
import java.util.Date;
import java.util.TreeMap;

public class DataUtility {
    public static TreeMap<Date,Double> convertAbsoluteValueToRelative(TreeMap<Date, Double> absoluteValues){
        TreeMap<Date, Double> relativeValues = absoluteValues; //map which will be returned
        double temp = 0; //set previous value to 0, so that the first relative value is equal to the absolute
        for (Date date : absoluteValues.keySet()) {
            relativeValues.put(date, (absoluteValues.get(date)-temp)); //calculate the relative using the previous value
            temp = absoluteValues.get(date); //set the previous value
        }
        return relativeValues;
    }

    public static TreeMap<Date, Double> getChangedValue(TreeMap<Date, Double> absoluteValues, TreeMap<Date, Double> relativeValues){
        TreeMap<Date, Double> changedValues = new TreeMap<>(); //map which will be returned
        double temp = 0;
        for (Date date : relativeValues.keySet()) { //iterate through map with relative values
            if (absoluteValues.containsKey(date)){ //check if absolute and relative values have a common key
                temp = absoluteValues.get(date); //set temp to be the value with the common key of relative and absolute
                changedValues.put(date, absoluteValues.get(date)); //set the changed value to be the absolute value
                continue; //skips setting the changed value to relative + absolute value
            }
            changedValues.put(date, (temp+relativeValues.get(date))); //set changed value to be relative + absolute value
        }
        return changedValues;
    }
}
