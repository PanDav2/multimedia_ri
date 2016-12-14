package upmc.ri.struct.model;

import upmc.ri.struct.instantiation.IStructInstantiation;

import java.util.Random;


abstract class LinearStructModel<X,Y> implements IStructModel<X,Y>  {
    public IStructInstantiation<X,Y> Structure;
    public double[] parameters;

    public IStructInstantiation<X,Y> instantiation(){return Structure;}

    public double[] getParameters(){return parameters;}

    public void setParameters(double[] parameter){parameters = parameter;}

}
