package compareDiceSets;

import java.util.ArrayList;
import java.util.List;

public class Statistics {

	public static <V extends Number & Comparable<V>> List<String> statsPack(List<V> data, String prefix) {
        List<String> statsString = new ArrayList<String>();
        statsString.add(prefix + minimum(data));
        statsString.add(prefix + maximum(data));
        statsString.add(prefix + mean(data));
        statsString.add(prefix + geometricMean(data));
        statsString.add(prefix + median(data));
        statsString.add(prefix + modeString(mode(data)));
        statsString.add(prefix + sampleVariance(data));
        statsString.add(prefix + sampleStandardDeviation(data));
        statsString.add(prefix + populationVariance(data));
        statsString.add(prefix + populationStandardDeviation(data));
        return statsString;
    }
    public static <V extends Number & Comparable<V>> List<String> statsPack(List<V> data) {
        return statsPack(data, "");
	}
    /**
	 * Finds the minimum value in the passed array. 
	 * NOTE: The array does not need to be completely populated. 
	 * All relevant data to be processed starts at index 0.
	 * Unused elements will be null and should not be 
	 * considered in computations. Once the first null is 
	 * encountered it is assumed remaining elements are unused.
	 * @param data - Collection of data to find minimum value of.
	 * @param <K> - Type of data passed. The type must implement the
	 * Comparable interface.
	 * @return Minimum value in passed data.
	 */
	public static <K extends Comparable<K>> K minimum(List<K> data) {
		K min = data.get(0);
		if (min == null)
			return null;
		for (K val : data) {
			if (val == null)
				break;
			else if (min.compareTo(val) >= 0)
				min = val;
		}
		return min;
    }
	
	/**
	 * Finds the maximum value in the passed array. 
	 * NOTE: The array does not need to be completely populated. 
	 * All relevant data to be processed starts at index 0. 
	 * Unused elements will be null and should not be 
	 * considered in computations. Once the first null is 
	 * encountered it is assumed remaining elements are unused.
	 * @param data - Collection of data to find maximum value of.
	 * @param <K> - Type of data passed. The type must implement 
	 * the Comparable interface.
	 * @return Maximum value in passed data.
	 */
	public static <K extends Comparable<K>> K maximum(List<K> data) {
		K max = data.get(0);
		if (max == null)
			return null;
		for (K val : data) {
			if (val == null)
				break;
			else if (max.compareTo(val) < 0)
				max = val;
		}
		return max;
	}
	
	/**
	 * Finds the average of values in the passed array. 
	 * NOTE: The array does not need to be completely populated. 
	 * All relevant data to be processed starts at index 0. 
	 * Unused elements will be null and should not be 
	 * considered in computations. Once the first null is 
	 * encountered it is assumed remaining elements are unused.
	 * @param data - Collection of data to find average of.
	 * @param <N> - Type of data passed. The type must extend the
	 * Number class. (See supplemental doc for info on num class)
	 * @return Average of passed data. Regardless of type passed,
	 * will always return a Double.
	 */
	public static <N extends Number> Double mean(List<N> data) {
		double sum = 0.0;
		int count = 0;
		if (data.get(0) == null)
			return Double.NaN;
		for (N val : data) {
			if (val == null)
				break;
			else {
				count++;
				sum += val.doubleValue();
			}
		}
		sum = sum / count;
		return sum;
    }

    /**
     * approximate. doubles are lame :(
     * @param <N>
     * @param data
     * @return
     */
    public static <N extends Number> Double geometricMean(List<N> data) {
        double product = 1;
        double size = data.size();
        for (N val : data) {
            product *= Math.pow(val.doubleValue(), 1.0 / size);
        }
        return product;
    }
    
    public static <N extends Number> Double median(List<N> data) {
        data.sort((N o1, N o2) -> 
            Double.compare(o1.doubleValue(), o2.doubleValue())
        );
        int size = data.size();
        return (size % 2 == 0)
            ? (data.get((size - 1) / 2).doubleValue() + data.get((size / 2)).doubleValue()) / 2
            : data.get(size / 2).doubleValue();
    }

    public static <N extends Number> List<Tuple<Integer, N>> frequency(List<N> data) {
        List<Tuple<Integer, N>> frequencies = new ArrayList<Tuple<Integer, N>>();
        for (N elem : data) {
            boolean exists = false;
            for (int i = 0; i < frequencies.size(); i++) {
                Tuple<Integer, N> temp = frequencies.get(i);
                if (temp.getSecond() == elem) {
                    exists = true;
                    temp.setFirst(temp.getFirst() + 1);
                    break;
                }
            }
            if (!exists) {
                frequencies.add(new Tuple<Integer, N>(1, elem));
            }
        }
        return frequencies;
    }

    public static <N extends Number> List<N> mode(List<N> data) {
        List<Tuple<Integer, N>> frequencies = frequency(data);
        List<N> maxes = new ArrayList<N>();
        Integer maxFrequency = 0;
        for (Tuple<Integer, N> elem : frequencies)
            if (maxFrequency < elem.getFirst())
                maxFrequency = elem.getFirst();
        for (Tuple<Integer, N> elem : frequencies)
            if (elem.getFirst() == maxFrequency)
                maxes.add(elem.getSecond());
        return maxes;
    }
    public static <N extends Number> String modeString(List<N> data) {
        StringBuilder modeString = new StringBuilder("");
        for (N val : data) {
            modeString.append(val.toString());
            modeString.append(", ");
        }
        return modeString.substring(0, modeString.length() - 2);
    }

    public static <N extends Number> Double populationVariance(List<N> data) {
        return standardDeviationVariancePopulation(data, true);
    }
    public static <N extends Number> Double populationStandardDeviation(List<N> data) {
        return standardDeviationVariancePopulation(data, false);
    }
    public static <N extends Number> Double sampleVariance(List<N> data) {
        return standardDeviationVarianceSample(data, true);
    }
    public static <N extends Number> Double sampleStandardDeviation(List<N> data) {
        return standardDeviationVarianceSample(data, false);
    }
	/**
	 * Finds the population standard deviation of values in the 
	 * passed array. NOTE: The array does not need to be 
	 * completely populated. All relevant data to be processed 
	 * starts at index 0. Unused elements will be null and should 
	 * not be considered in computations. Once the first null 
	 * is encountered it is assumed remaining elements are unused.
	 * @param data - Collection of data to find standard deviation of.
	 * @param <N> - Type of data passed. The type must extend the 
	 * Number class. See supplemental doc for info
	 * @return Poplation standard deviation of passed data. Regardless
	 * of type passed, will always return a Double.
	 */
	public static <N extends Number> Double standardDeviationVariancePopulation(List<N> data, boolean variance) {
		double average = mean(data);
		double residuals = 0;
		int count = 0;
		if (data.get(0) == null)
			return Double.NaN;
		for (N val : data) {
			if (val == null)
				break;
			else {
				count++;
				residuals = residuals + Math.pow(val.doubleValue() - average, 2);
			}
		}
		residuals = residuals / count;
        if (!variance)
            residuals = Math.sqrt(residuals);
		return residuals;
    }
    
    public static <N extends Number> Double standardDeviationVarianceSample(List<N> data, boolean variance) {
		double average = mean(data);
		double residuals = 0;
        int count = 0;
        if (data.size() == 1)
            return 0.0;
		if (data.get(0) == null)
			return Double.NaN;
		for (N val : data) {
			if (val == null)
				break;
			else {
				count++;
				residuals = residuals + Math.pow(val.doubleValue() - average, 2);
			}
        }
		residuals = residuals / (count - 1);
        if (!variance)
            residuals = Math.sqrt(residuals);
		return residuals;
    }

    /**
     * https://en.wikipedia.org/wiki/Pooled_variance
     * https://sisu.ut.ee/sites/default/files/measurement/files/pooled_standard_deviation.pdf
     * @param <N>
     * @param data
     * @return
     */
    public static <N extends Number> Double pooledSampleVariance(List<List<N>> data) {
        return pooledStandardDeviationVarianceSample(data, true);
    }
    public static <N extends Number> Double pooledSampleStandardDeviation(List<List<N>> data) {
        return pooledStandardDeviationVarianceSample(data, false);
    }
    public static <N extends Number> Double pooledStandardDeviationVarianceSample(List<List<N>> data, boolean variance) {
        double dividend = 0.0;
        double divisor = 0.0;
        for (List<N> sample : data) {
            dividend += sampleVariance(sample) * (sample.size() - 1);
            divisor += sample.size();
        }
        return variance
            ? dividend / (divisor - data.size())
            : Math.sqrt(dividend / (divisor - data.size()));
    }

    //Arrays


    /**
	 * Finds the minimum value in the passed array. 
	 * NOTE: The array does not need to be completely populated. 
	 * All relevant data to be processed starts at index 0.
	 * Unused elements will be null and should not be 
	 * considered in computations. Once the first null is 
	 * encountered it is assumed remaining elements are unused.
	 * @param data - Collection of data to find minimum value of.
	 * @param <K> - Type of data passed. The type must implement the
	 * Comparable interface.
	 * @return Minimum value in passed data.
	 */
	public static <K extends Comparable<K>> K minimum(K[] data)
	{
		K min = data[0];
		if (min == null)
		{
			return null;
		}
		for (int i = 0; i < data.length; i++)
		{
			if (data[i] == null)
			{
				break;
			}
			else if (min.compareTo(data[i]) >= 0)
			{
				min = data[i];
			}
		}
		return min;
    }
    /**
	 * Finds the maximum value in the passed array. 
	 * NOTE: The array does not need to be completely populated. 
	 * All relevant data to be processed starts at index 0. 
	 * Unused elements will be null and should not be 
	 * considered in computations. Once the first null is 
	 * encountered it is assumed remaining elements are unused.
	 * @param data - Collection of data to find maximum value of.
	 * @param <K> - Type of data passed. The type must implement 
	 * the Comparable interface.
	 * @return Maximum value in passed data.
	 */
    public static <K extends Comparable<K>> K maximum(K[] data)
	{
		K max = data[0];
		if (max == null)
		{
			return null;
		}
		for (int i = 0; i < data.length; i++)
		{
			if (data[i] == null)
			{
				break;
			}
			else if (max.compareTo(data[i]) < 0)
			{
				max = data[i];
			}
		}
		return max;
	}
    /**
	 * Finds the average of values in the passed array. 
	 * NOTE: The array does not need to be completely populated. 
	 * All relevant data to be processed starts at index 0. 
	 * Unused elements will be null and should not be 
	 * considered in computations. Once the first null is 
	 * encountered it is assumed remaining elements are unused.
	 * @param data - Collection of data to find average of.
	 * @param <N> - Type of data passed. The type must extend the
	 * Number class. (See supplemental doc for info on num class)
	 * @return Average of passed data. Regardless of type passed,
	 * will always return a Double.
	 */
	public static <N extends Number> Double meanArray(N[] data)
	{
		double sum = 0.0;
		int count = 0;
		if (data[0] == null)
		{
			return Double.NaN;
		}
		for (int i = 0; i < data.length; i++)
		{
			if (data[i] == null)
			{
				break;
			}
			else 
			{
				count++;
				sum += data[i].doubleValue();
			}
		}
		sum = sum / count;
		return sum;
    }
    public static <N extends Number> Double populationVarianceArray(N[] data) {
        return standardDeviationVariancePopulationArray(data, true);
    }
    public static <N extends Number> Double populationStandardDeviationArray(N[] data) {
        return standardDeviationVariancePopulationArray(data, false);
    }
    public static <N extends Number> Double sampleVarianceArray(N[] data) {
        return standardDeviationVarianceSampleArray(data, true);
    }
    public static <N extends Number> Double sampleStandardDeviationArray(N[] data) {
        return standardDeviationVarianceSampleArray(data, false);
    }
    public static <N extends Number> Double standardDeviationVariancePopulationArray(N[] data, boolean variance)
	{
		//https://wikimedia.org/api/rest_v1/media/math/render/svg/00eb0cde84f0a838a2de6db9f382866427aeb3bf
		double average = meanArray(data);
		double residuals = 0;
		int count = 0;
		if (data[0] == null)
		{
			return Double.NaN;
		}
		for (int i = 0; i < data.length; i++)
		{
			if (data[i] == null)
			{
				break;
			}
			else
			{
				count++;
				//Standard deviation involves the sum of the residuals squared
				residuals = residuals + Math.pow(data[i].doubleValue() - average, 2);
			}
		}
		residuals = residuals / count;
        if (!variance)
            residuals = Math.sqrt(residuals);
		return residuals;
    }
    public static <N extends Number> Double standardDeviationVarianceSampleArray(N[] data, boolean variance)
	{
		//https://wikimedia.org/api/rest_v1/media/math/render/svg/00eb0cde84f0a838a2de6db9f382866427aeb3bf
		double average = meanArray(data);
		double residuals = 0;
        int count = 0;
        if (data.length == 1)
            return 0.0;
		if (data[0] == null)
		{
			return Double.NaN;
		}
		for (int i = 0; i < data.length; i++)
		{
			if (data[i] == null)
			{
				break;
			}
			else
			{
				count++;
				//Standard deviation involves the sum of the residuals squared
				residuals = residuals + Math.pow(data[i].doubleValue() - average, 2);
			}
        }
		residuals = residuals / (count - 1);
        if (!variance)
            residuals = Math.sqrt(residuals);
		return residuals;
	}
}