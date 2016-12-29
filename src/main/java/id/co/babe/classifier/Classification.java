package id.co.babe.classifier;

import java.util.Collection;

/**
 * A basic wrapper reflecting a classification.  
 * It will store both feature-set and resulting classification.
 *
 *
 * @param <T> The feature class.
 * @param <K> The category class.
 */
public class Classification<T, K> {

    /**
     * The classified feature-set.
     */
    private Collection<T> featureset;

    /**
     * The category - feature-set was classified.
     */
    private K category;

    /**
     * The probability feature-set belongs category.
     */
    private double probability;

    /**
     * Constructs new Classification default probability of 1.
     *
     * @param featureset The feature-set.
     * @param category The category.
     */
    public Classification(Collection<T> featureset, K category) {
        this(featureset, category, 1.0f);
    }

    /**
     * Constructs a new Classification with the parameters given.
     *
     * @param featureset : feature-set.
     * @param category : category.
     * @param probability : probability.
     */
    public Classification(Collection<T> featureset, K category,
            double probability) {
        this.featureset = featureset;
        this.category = category;
        this.probability = probability;
    }

    /**
     * Retrieves the feature-set classified.
     *
     * @return The feature-set.
     */
    public Collection<T> getFeatureset() {
        return featureset;
    }

    /**
     * Retrieves the classification's probability.
     * @return
     */
    public double getProbability() {
        return this.probability;
    }

    /**
     * Retrieves the category the feature-set was classified as.
     *
     * @return The category.
     */
    public K getCategory() {
        return category;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Classification [category=" + this.category
                + ", probability=" + this.probability
                + ", featureset=" + this.featureset
                + "]";
    }

}
