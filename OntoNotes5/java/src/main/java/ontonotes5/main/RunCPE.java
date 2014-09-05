package ontonotes5.main;

import ontonotes5.common.StatsCollector;

import org.apache.uima.UIMAFramework;
import org.apache.uima.collection.CollectionProcessingEngine;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.util.XMLInputSource;


public class RunCPE {
  public static void main(final String[] args) throws Exception {
    // Parse CPE descriptor in file specified on command line.
    String cpeFile = args[0];
    CpeDescription cpeDesc = UIMAFramework.getXMLParser().parseCpeDescription(new XMLInputSource(cpeFile));

    // Instantiate the CPE and setup the status callback listener.
    CollectionProcessingEngine cpe = UIMAFramework.produceCollectionProcessingEngine(cpeDesc);
    cpe.addStatusCallbackListener(new StatsCollector.StatsCollectorStatusCallbackListener());

    //Start Processing
    cpe.process();
  }
}
