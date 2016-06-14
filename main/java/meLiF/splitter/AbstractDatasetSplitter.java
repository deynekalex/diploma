package meLiF.splitter;

import org.slf4j.LoggerFactory;


/**
 * @author iisaev
 */
public abstract class AbstractDatasetSplitter implements DatasetSplitter {
    protected final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
}
