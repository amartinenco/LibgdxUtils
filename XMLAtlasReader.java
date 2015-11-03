package com.mygdx.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

import java.io.FileNotFoundException;

public class XMLAtlasReader {

    private FileHandle atlas;
    private boolean exists = false;
    private String filePath;
    private Texture texture;

    public XMLAtlasReader(Texture texture, String path) {
        this.texture = texture;
        this.filePath = path;
        atlas = new FileHandle(filePath);
        if (atlas.exists()) {
            exists = true;
        }
        else {
            Gdx.app.error("Xml->TextureRegion:", "Unable to locate: " + filePath,
                                                new FileNotFoundException("Unable to find an XML for parsing!"));
        }
    }

    public TextureRegion findTextureRegion(String regionName) {
        XmlReader reader = new XmlReader();
        TextureRegion region = null;

        if (!exists) return null;

        try {
            XmlReader.Element root = reader.parse(atlas);
            Array<XmlReader.Element> subTextures = root.getChildrenByName("SubTexture");
            for (XmlReader.Element element : subTextures)
            {
                if (element.getAttribute("name").equals(regionName)) {
                    Gdx.app.debug("Xml->TextureRegion:", "Loading: " + element.getAttribute("name") + " " + element.getAttribute("x") +
                                                " " + element.getAttribute("y") + " " + element.getAttribute("width") +
                                            " " + element.getAttribute("height") );

                    region = new TextureRegion(texture, Integer.parseInt(element.getAttribute("x")),
                                                        Integer.parseInt(element.getAttribute("y")),
                                                        Integer.parseInt(element.getAttribute("width")),
                                                        Integer.parseInt(element.getAttribute("height")));
                    break;
                }
            }
        }
        catch (Exception ex) {
            throw new ParseXmlAtlasException("Could not parse XML file: " + atlas.file().getName());
        }
        return region;
    }
}
