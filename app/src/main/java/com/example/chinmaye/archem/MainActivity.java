package com.example.chinmaye.archem;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.*;
import java.util.*;

import com.google.android.filament.MaterialInstance;
import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Trackable;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import archem.entities.Atom;
import archem.entities.Molecule;
import archem.entities.MoleculeCollection;

public class MainActivity extends AppCompatActivity
{
    private ArFragment arFragment;
    TextView tv;
    String formula;
    private static final float scale = 300.0f;

    @SuppressLint("VisibleForTests")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textView);
        formula = getIntent().getStringExtra("formula");
        tv.setText(formula);
        Toast.makeText(getApplicationContext(), formula, Toast.LENGTH_SHORT).show();

        //......................
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

        ArSceneView sv = arFragment.getArSceneView();
        Frame frame = sv.getArFrame();
        Scene scene = sv.getScene();


//        Molecule kcl= MoleculeCollection.getMolecule("KCl");
//
//        System.out.println(hcl.toString());
//        System.out.println(kcl.toString());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("test1", "onClick");
//                addObject(Uri.parse("halo.sfb"));
//                addObject(Uri.parse("halo.sfa"));
//                MaterialFactory.makeTransparentWithColor(getApplicationContext(), new com.google.ar.sceneform.rendering.Color(1, 1, 1,1)).thenAccept(material ->
                MaterialFactory.makeOpaqueWithColor(getApplicationContext(), new com.google.ar.sceneform.rendering.Color(1, 1, 1,1)).thenAccept(material ->
                {
                    Log.d("test1", "creating molecule " + material);
                    createMolecule(material);
                });
            }
        });

    }

    private void createMolecule(Material material)
    {

//        AnchorNode anchorNode = getAnchorNode(material);
//        Log.d("test1","befor creating sphere");
//        createSphere((float)0.1f, 0.1f, (float)0.1f, .05f, anchorNode,material);
//        Log.d("test1","sphere1");
//        createSphere((float)0.3f, 0.3f, (float)0.2f, .05f, anchorNode,material);
//        Log.d("test1","sphere2");
//        createSphere((float)0.2f, 0.2f, (float)0.2f, .05f, anchorNode,material);
//        Log.d("test1","sphere3");
//        createSphere((float)0.5f, 0.5f, (float)0.1f, .05f, anchorNode,material);
//        Log.d("test1","sphere4");
//        createSphere((float)0.9f, 0.1f, (float)0.4f, .05f, anchorNode,material);
//        Log.d("test1","sphere5");
//        createSphere((float)0.2f, 0.2f, (float)0.2f, .05f, anchorNode,material);
//        Log.d("test1","sphere6");


        Molecule molecule = MoleculeCollection.getMolecule(formula);
        AnchorNode anchorNode = getAnchorNode(material);

        //AnchorNode anchorNode = getAnchorNode(material);
        Log.d("test1", "achorNode" + anchorNode);

        Material nucleusMaterial = material.makeCopy();
        Color nucleusColor = new Color(1, 1, 0, .0f);
        nucleusMaterial.setFloat4("color", nucleusColor);

        Material protonMaterial = material.makeCopy();
        Color protonColor = new Color(1, 0, 0, 1f);
        protonMaterial.setFloat4("color", protonColor);

        Material neutronMaterial = material.makeCopy();
        Color neutronColor = new Color(0, 0, 1, 1f);
        neutronMaterial.setFloat4("color", neutronColor);

        Random r = new Random();
        for (Atom a : molecule.atoms)
        {

            Log.d("test1", "creating atom");
            Node atomNode = createSphere((float) (a.x) / scale, 0f, (float) a.y / scale, .025f, anchorNode, nucleusMaterial);

            for (int i = 0; i < a.protons; i++)
            {
                float x = (r.nextFloat() * 2) - 1;
                float y = (r.nextFloat() * 2) - 1;
                float z = (r.nextFloat() * 2) - 1;
                createSphere((a.x + x) / scale, (0 + y) / scale, (a.y + z) / scale, 0.0025f, atomNode, protonMaterial);
            }
            for (int i = 0; i < a.neutrons; i++)
            {
                float x = (r.nextFloat() * 2) - 1;
                float y = (r.nextFloat() * 2) - 1;
                float z = (r.nextFloat() * 2) - 1;
                createSphere((a.x + x) / scale, (0 + y) / scale, (a.y + z) / scale, 0.0025f, atomNode, neutronMaterial);
            }

//            //add rings
//            for (int i = 0; i < a.configuration.length; i++)
//            {
//                Material m = nucleusMaterial.makeCopy();
//                m.setFloat3("color", new Color(.2f + i / 10f, .2f + i / 10f, .2f + i / 10f));
//                TransformableNode atomNode1 = createCylinder((float) a.x / scale, 0f, (float) a.y / scale, (i + 1) * 10 / scale, 0.001f * (a.configuration.length - i), atomNode, m);
//            }


            for (Atom.Electron e : a.electrons)
            {
                Log.d("test1", "creating electron");
//                createSphere((float) (e.x+a.x)/scale, 0f, (float) (a.y+e.y)/scale, .0005f, atomNode,material);
                createSphere((float) (e.x + a.x) / scale, 0f, (float) (e.y + a.y) / scale, .005f, atomNode, material);
            }
        }
    }

    private AnchorNode getAnchorNode(Material material)
    {
        Log.d("test1", "getAnchorNode");
        Frame frame = arFragment.getArSceneView().getArFrame();
        Point point = getScreenCenter();
        Log.d("test1", "SC " + point.toString());
        if (frame != null)
        {
            final List<HitResult> hits = frame.hitTest((float) point.x, (float) point.y);
            Log.d("test1", "Hits :" + hits.toString());
            if (!hits.isEmpty())
            {
                ModelRenderable sphere1 = ShapeFactory.makeSphere(0f, new Vector3(0, 0, 0), material);
                Anchor a = hits.get(0).createAnchor();
                AnchorNode anchorNode = new AnchorNode(a);
//                TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
//                transformableNode.setRenderable(sphere1);
//                transformableNode.setParent(anchorNode);
//                //anchorNode.setLocalScale(new Vector3(.1f, .1f, .1f));
//                arFragment.getArSceneView().getScene().addChild(anchorNode);
//                transformableNode.select();

                return anchorNode;
            }

        }
        return null;
    }

    private Node createSphere2(float x, float y, float z, float radius, Node parent, Material material)
    {
        Log.d("test1",String.format("Create Sphere : %f %f %f %f",x,y,z,radius));
//        Node ren=new Node();
        TransformableNode ren=new TransformableNode(arFragment.getTransformationSystem());
        //transformableNode.setRenderable(sphere);
        ModelRenderable sphere2 = ShapeFactory.makeSphere(radius, new Vector3(x,y,z), material);
        ren.setRenderable(sphere2);
        ren.setParent(parent);
        parent.addChild(ren);
//        ren.setLocalPosition(new Vector3(x,y,z));
//        ren.setLocalRotation(new Quaternion(0,0,0,0));
//        ren.setLocalScale(new Vector3(1,1,1));

        return ren;
    }

    private TransformableNode createSphere(float x, float y, float z, float radius, Node parent, Material material)
    {
        Log.d("test1", "createSphere: " + x + ", " + y + ", " + z + ", " + radius + ", " + parent);
        ModelRenderable sphere = ShapeFactory.makeSphere(radius, new Vector3(x,y,z), material);
        Log.d("test1", "aftercreateSphere: " + x + ", " + y + ", " + z + ", " + radius + ", " + parent);
        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setRenderable(sphere);
        transformableNode.setParent(parent);
        parent.addChild(transformableNode);
//        transformableNode.setLocalPosition(new Vector3(0.5f,0.5f,0.25f));
        // ModelRenderable.setLocalScale(new Vector3(.1f, .1f, .1f));
        arFragment.getArSceneView().getScene().addChild(transformableNode);
        transformableNode.select();
        Log.d("test1", "sphere has created");
        return transformableNode;
    }

    private TransformableNode createCylinder(float x, float y, float z, float radius, float height, Node parent, Material material)
    {

//        Log.d("test1", "createSphere: "+x+", "+y+", "+z+", "+radius+", "+parent);
        ModelRenderable sphere = ShapeFactory.makeCylinder(radius, height, new Vector3(x, y, z), material);
//        Log.d("test1", "aftercreateSphere: "+x+", "+y+", "+z+", "+radius+", "+parent);
        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setRenderable(sphere);
        transformableNode.setParent(parent);
        parent.addChild(transformableNode);
        // ModelRenderable.setLocalScale(new Vector3(.1f, .1f, .1f));
        //arFragment.getArSceneView().getScene().addChild(transformableNode);
        transformableNode.select();
//        Log.d("test1", "cylinder has created");
        return transformableNode;

    }

    @Override
    public String toString()
    {
        return "MainActivity{" +
                "arFragment=" + arFragment +
                '}';
    }

    private void addObject(Uri parse)
    {
        Log.d("test1", "adding object");
        Frame frame = arFragment.getArSceneView().getArFrame();
        Point point = getScreenCenter();
        Log.d("test1", point.toString());
        if (frame != null)
        {
            final List<HitResult> hits = frame.hitTest((float) point.x, (float) point.y);
            Log.d("test1", "Hits :" + hits.toString());

            for (int i = 0; i < hits.size(); i++)
            {
                final int j = i;
                Trackable trackable = hits.get(i).getTrackable();
                if (trackable instanceof Plane && ((Plane) trackable).isPoseInPolygon(hits.get(i).getHitPose()))
                {
//                    MaterialFactory.makeOpaqueWithColor(this, new com.google.ar.sceneform.rendering.Color(1,1,1)).thenAccept(material ->
//                    {
//                        ModelRenderable sphere = ShapeFactory.makeSphere(.1f, new Vector3(0, 0, 0), material);
//                        ModelRenderable ring=ShapeFactory.makeCylinder(.1f,.05f,new Vector3(0,.5f,0),material);
//                        addNode(arFragment,hits.get(j).createAnchor(),ring);
//                    } );

                    placeObject(arFragment, hits.get(i).createAnchor(), parse);
                }
            }
        }
    }

    private void placeObject(final ArFragment fragment, final Anchor createAnchor, Uri model)
    {
        Log.d("test1", "placing object");
        ModelRenderable.builder().setSource(fragment.getContext(), model).build().thenAccept((new Consumer()
        {
            public void accept(Object var1)
            {
                this.accept((ModelRenderable) var1);
            }

            public final void accept(ModelRenderable it)
            {
                if (it != null)
                    MainActivity.this.addNode(arFragment, createAnchor, it);
                else
                    Log.d("test1", "it==null");
            }
        })).exceptionally((new Function()
        {
            public Object apply(Object var1)
            {
                return this.apply((Throwable) var1);
            }

            @Nullable
            public final Void apply(Throwable it)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(it.getMessage()).setTitle("error!");
                AlertDialog dialog = builder.create();
                dialog.show();
                return null;
            }
        }));
    }

    private void addNode(ArFragment fragment, Anchor createAnchor, ModelRenderable renderable)
    {
        Log.d("test1", "adding node");

        AnchorNode anchorNode = new AnchorNode(createAnchor);
        TransformableNode transformableNode = new TransformableNode(fragment.getTransformationSystem());
        transformableNode.setRenderable(renderable);
        transformableNode.setParent(anchorNode);
        anchorNode.setLocalScale(new Vector3(.1f, .1f, .1f));
        fragment.getArSceneView().getScene().addChild(anchorNode);
        transformableNode.select();
    }

    private Point getScreenCenter()
    {
        View vw = findViewById(android.R.id.content);
        return new Point(vw.getWidth() / 2, vw.getHeight() / 2);
    }

}
