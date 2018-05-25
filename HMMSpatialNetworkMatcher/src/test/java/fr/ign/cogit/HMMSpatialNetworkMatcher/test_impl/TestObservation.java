package fr.ign.cogit.HMMSpatialNetworkMatcher.test_impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import fr.ign.cogit.HMMSpatialNetworkMatcher.api.IEmissionProbablityStrategy;
import fr.ign.cogit.HMMSpatialNetworkMatcher.api.IHiddenState;
import fr.ign.cogit.HMMSpatialNetworkMatcher.impl.HiddenState;
import fr.ign.cogit.HMMSpatialNetworkMatcher.impl.HiddenStatePopulation;
import fr.ign.cogit.HMMSpatialNetworkMatcher.impl.Observation;
import fr.ign.cogit.HMMSpatialNetworkMatcher.matching.core.ParametersSet;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;

public class TestObservation {
  
  static Observation obs;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    obs = Mockito.mock(Observation.class, Mockito.CALLS_REAL_METHODS);
  }


  @Test
  public void testEmissionProbability() {
    IHiddenState state = Mockito.mock(IHiddenState.class);
    
    IEmissionProbablityStrategy e = Mockito.mock(IEmissionProbablityStrategy.class);
    obs.setEmissionProbaStrategy(e);
    Mockito.when(e.compute(obs, state)).thenReturn(10.);

    assertEquals(obs.computeEmissionProbability(state), 10., 0.0001);
  }
  
  @Test
  public void testCandidates() {
HiddenStatePopulation popH = new HiddenStatePopulation();
    
    IDirectPosition p1 = new DirectPosition(0,0);
    IDirectPosition p2 = new DirectPosition(1,1);
    IDirectPosition p3 = new DirectPosition(0,1);
    IDirectPosition p4 = new DirectPosition(1,0);
    IDirectPosition p5 = new DirectPosition(3,3);
    IDirectPosition p6 = new DirectPosition(4,4);
    IDirectPosition p7 = new DirectPosition(0.25,1);
    IDirectPosition p8 = new DirectPosition(0.75,1);


    
    HiddenState s1 = new HiddenState(new GM_LineString(
        new DirectPositionList(Arrays.asList(p1,p2))));
    HiddenState s2 = new HiddenState(new GM_LineString(
        new DirectPositionList(Arrays.asList(p1,p3))));
    HiddenState s3 = new HiddenState(new GM_LineString(
        new DirectPositionList(Arrays.asList(p1,p4))));
    HiddenState s4 = new HiddenState(new GM_LineString(
        new DirectPositionList(Arrays.asList(p5,p6))));
    
    popH.add(s1);
    popH.add(s2);
    popH.add(s3);
    popH.add(s4);
    
    Mockito.when(obs.getGeom()).thenReturn(new GM_LineString(new DirectPositionList(Arrays.asList(p7,p8))));
    
    ParametersSet.get().SELECTION_THRESHOLD = 1;
    Collection<IHiddenState>result = obs.candidates(popH);
    assertEquals(result.size(), 3);
    assertTrue(result.contains(s1));
    assertTrue(result.contains(s2));
    assertTrue(result.contains(s3));
    assertTrue(!result.contains(s4));
  }

}
