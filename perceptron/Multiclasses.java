package monPerceptron;

import java.util.ArrayList;
import java.util.Random;
 
import image.ImageConverter;
import mnisttools.MnistReader;
 
public class Multiclasses {
 
	/* Les donnees */
	public static String path="";
	public static String labelDB=path+"train-labels-idx1-ubyte";
	public static String imageDB=path+"train-images-idx3-ubyte";
 
	/* Parametres */
	// les N premiers exemples pour l'apprentissage (ou "l'etape de training")
	public static final int N = 10000;
	// les T derniers exemples  pour l'evaluation (ou "l'étape de test")
	public static final int T = 10000; 
	// Nombre d'epoque max
	public final static int EPOCHMAX=100; 
	// Learning rate (ou "taux d'apprentissage")
	public static float lr=(float) 0.1; 
	//? public static int s_batch = 20;
 
	public static void Load(ArrayList<float[]> trainData, ArrayList<Integer> trainRefs, ArrayList<float[]> testData, ArrayList<Integer> testRefs)
	{
		System.err.println("# Load the database !");
		/* Lecteur d'image */ 
		MnistReader db = new MnistReader(labelDB, imageDB);
		/* Taille des images et donc de l'espace de representation */
		final int SIZEW = ImageConverter.image2VecteurReel_withB(db.getImage(1)).length;
 
 
		/* Creation des donnees */
		/* Donnees d'apprentissage */
		for (int i = 1; i <= N; i++) {
			trainData.add(ImageConverter.image2VecteurReel_withB(db.getImage(i)));
			trainRefs.add(db.getLabel(i));
		}
 
		for (int i = N+1; i <= N+T+1; i++) {
			testData.add(ImageConverter.image2VecteurReel_withB(db.getImage(i)));
			testRefs.add(db.getLabel(i));
		}
	}
	
	/*public class Cell {
		float input[] = new float[28*28];
		float weight[] = new float[28*28];
		float ouput;
	}
	
	public class Layer {
		Cell cell[] = new Cell[10];
	}*/
	
	public static void initLayer(Layer l) {
		for (int a=0; a<10; a++) {
			for(int b=0; b<(28*28); b++) {
				l.cell[a].input[b] = 0;
				l.cell[a].weight[b] = (float) Math.random();//Math.random() rend un double->faut le convertir en float
			}
		}
	}
	
	public static int[] targetOutput(int lbl) {
		int[] v  = new int[10];
		for (int i=0; i<10; i++){
			if (i==lbl) {
				v[i] = 1;
			}
			else {v[i] = 0;}
		}
		return v;
	}
	
	public static void setCellInput(Cell c, float[] img) {
		for (int i=0; i<(28*28); i++){
			c.input[i] = /*(float)*/img[i];//:255;//normaliser output (0->1) ?
		}
	}
	
	public static void calcCelloutput(Cell c) {
		c.output = 0;
		for (int i=0; i<(28*28); i++){
			c.output += c.input[i] * c.weight[i];
		}
    //c.output /= (28*28);//normaliser output (0->1) ?
	}
	
	public static float[] tablSoftmax(Layer l){
		float[] v  = new float[10];
		float sum = 0;
		for (int a=0; a<10; a++){
	        sum += Math.exp(l.cell[a].output);
	    }
		for (int b=0; b<10; b++){
	        v[b] = (float) Math.exp(l.cell[b].output)/sum;
	    }
	    return v; 
	}
	
	public static void updateCellWeight(Cell c, float q, float p){
		for (int i=0; i<(28*28); i++){
	        c.weight[i] = c.weight[i] - lr*c.input[i]*(q-p);
	    }
	}
	
	public static void updateAllCellWeight(Layer l, int lbl){
		float[] v = tablSoftmax(l);
		int[] label = targetOutput(lbl);
		for (int i=0; i<10; i++){
			updateCellWeight(l.cell[i], v[i], label[i]);
		}
	}
	
	public static int getLayerPrediction(Layer l){
	    float maxOut = 0;
	    int maxInd = 0;
	    float[] v = tablSoftmax(l);
	    for (int i=0; i<10; i++){
	        if (v[i] > maxOut){
	            maxOut = v[i];
	            maxInd = i;
	        }
	    }
	    return maxInd; 
	}
	
	public static void main(String[] args) {
		System.out.println("Start ...");
		ArrayList<float[]> trainData = new ArrayList<float[]>();
		ArrayList<float[]> testData  = new ArrayList<float[]>();
		ArrayList<Integer> trainRefs = new ArrayList<Integer>();
		ArrayList<Integer> testRefs  = new ArrayList<Integer>();
		Load(trainData, trainRefs, testData, testRefs);
		
		Layer L = new Layer();
		initLayer(L);
		
		int NbErreurTrain = 0;
		for (int t=0; t<EPOCHMAX; t++) {
			for(int n=0; n<N; n++) {
				float[] trainImg = trainData.get(n);
				int[] vectorTarget = targetOutput(trainRefs.get(n));
				for (int i=0; i<10; i++) {
					setCellInput(L.cell[i], trainImg);
					calcCelloutput(L.cell[i]);
					updateAllCellWeight(L, trainRefs.get(n));
				}
				int deviner = getLayerPrediction(L);
				if (deviner != trainRefs.get(n)) { NbErreurTrain++; }
			}
		}
		
		int NbErreurTest = 0;
		for (int t=0; t<EPOCHMAX; t++) {
			for(int n=0; n<T; n++) {
				float[] testImg = testData.get(n);
				int[] vectorTarget = targetOutput(testRefs.get(n));
				for (int i=0; i<10; i++) {
					setCellInput(L.cell[i], testImg);
					calcCelloutput(L.cell[i]);
				}
				int deviner = getLayerPrediction(L);
				if (deviner != testRefs.get(n)) { NbErreurTest++; }
			}
		}
		
		System.out.println("Entrainement : "+NbErreurTrain+" erreurs sur "+N+" essais, soit "+(N-NbErreurTrain)/N*100+" % vraies");
		System.out.println("Test : "+NbErreurTest+" erreurs sur "+T+" essais, soit "+(T-NbErreurTrain)/T*100+" % vraies");
		
	}

}
 

