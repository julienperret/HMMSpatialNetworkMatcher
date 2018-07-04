package fr.ign.cogit.HMMSpatialNetworkMatcher.spatial_impl;

import fr.ign.cogit.HMMSpatialNetworkMatcher.api.CompositeEmissionProbabilityStrategy;
import fr.ign.cogit.HMMSpatialNetworkMatcher.api.ITransitionProbabilityStrategy;
import fr.ign.cogit.HMMSpatialNetworkMatcher.api.PathBuilder;
import fr.ign.cogit.HMMSpatialNetworkMatcher.api.PostProcessStrategy;
import fr.ign.cogit.HMMSpatialNetworkMatcher.spatial_impl.ep.DirectionDifferenceEmissionProbability;
import fr.ign.cogit.HMMSpatialNetworkMatcher.spatial_impl.ep.FrechetEmissionProbability;
import fr.ign.cogit.HMMSpatialNetworkMatcher.spatial_impl.ep.LineMedianDistanceEmissionProbability;
import fr.ign.cogit.HMMSpatialNetworkMatcher.spatial_impl.pathbuilder.StrokePathBuilder;
import fr.ign.cogit.HMMSpatialNetworkMatcher.spatial_impl.postProcessStrategy.OptimizationPostStratregy;
import fr.ign.cogit.HMMSpatialNetworkMatcher.spatial_impl.spatial_hmm.HMMMatchingLauncher;
import fr.ign.cogit.HMMSpatialNetworkMatcher.spatial_impl.spatial_hmm.ParametersSet;
import fr.ign.cogit.HMMSpatialNetworkMatcher.spatial_impl.tp.AngularTransitionProbability;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class LaunchExemple {
  
  public static void main(String[] args) {
    // Disable Logger messages
    Logger.getRootLogger().setLevel(Level.OFF);
    
    // First network
//    String fileNetwork1 ="/home/bcostes/Documents/IGN/these/donnees/vecteur/filaires/filaires_corriges"
//        + "/verniquet_l93_utf8_corr.shp";
//    
//    // Second network
//    String fileNetwork2 ="/home/bcostes/Documents/IGN/these/donnees/vecteur/filaires/filaires_corriges"
//        + "/jacoubet_l93_utf8.shp";
    
    String fileNetwork1 ="/home/bcostes/Documents/IGN/articles/article_appariement2/matchings/manual_matching"
        + "/snapshot_1784.0_1791.0_edges.shp";
    
    // Second network
    String fileNetwork2 ="/home/bcostes/Documents/IGN/articles/article_appariement2/matchings/manual_matching"
        + "/snapshot_1825.0_1836.0_edges.shp";
    
    // Emission probability stratregy
    // If you want to use more than one criteria, use CompositeEmissionProbability to wrap them
    CompositeEmissionProbabilityStrategy epStrategy = new CompositeEmissionProbabilityStrategy();
   // epStrategy.add(new LineMedianDistanceEmissionProbability(), 1.);
    epStrategy.add(new FrechetEmissionProbability(1), 1.);
   // epStrategy.add(new DirectionDifferenceEmissionProbability(), 1.);
    
    
    // Transition probability Strategy
    ITransitionProbabilityStrategy tpStrategy = new AngularTransitionProbability(1);
    
    // How to build the paths of the HMM ?
    PathBuilder pathBuilder = new StrokePathBuilder();
    
    // How to manage unexpected matched entities ?
    PostProcessStrategy postProcressStrategy = new OptimizationPostStratregy();
    
    // Parameters of the algorithm
    ParametersSet.get().SELECTION_THRESHOLD = 25;
    ParametersSet.get().NETWORK_PROJECTION = false;
    ParametersSet.get().PATH_MIN_LENGTH = 5;
    
    // Launcher
    HMMMatchingLauncher matchingLauncher = new HMMMatchingLauncher(fileNetwork1, fileNetwork2, epStrategy,
        tpStrategy, pathBuilder, postProcressStrategy, true);

    // Execute the HMM matching algorithm
    matchingLauncher.lauchMatchingProcess();
    
    // Export result
    matchingLauncher.exportMatchingResults("/home/bcostes/Bureau/test2");
  }

}
