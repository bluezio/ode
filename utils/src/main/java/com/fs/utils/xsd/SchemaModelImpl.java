/*
 * File:      $RCSfile$
 * Copyright: (C) 1999-2005 FiveSight Technologies Inc.
 *
 */
package com.fs.utils.xsd;

import com.sun.org.apache.xerces.internal.dom.DOMInputImpl;
import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaLoader;
import com.sun.org.apache.xerces.internal.xs.LSInputList;
import com.sun.org.apache.xerces.internal.xs.XSModel;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;

import org.w3c.dom.ls.LSInput;

/**
 * Xerces based schema model.
 */
public class SchemaModelImpl implements SchemaModel {
  private XSModel _model;

  private SchemaModelImpl(XSModel model) {
    if (model == null) {
      throw new NullPointerException("Null model.");
    }

    _model = model;
  }

  /**
   * Generate a schema model from a collection of schemas.
   * @param schemas collection of schemas (indexed by systemId)
   *
   * @return a {@link SchemaModel}
   */
  public static final SchemaModel newModel(Map<URI, byte[]> schemas) {
    XMLSchemaLoader schemaLoader = new XMLSchemaLoader();

    final String[] uris = new String[schemas.size()];
    final byte[][] content = new byte[schemas.size()][];

    int idx = 0;
    for (Iterator<Map.Entry<URI,byte[]>> i = schemas.entrySet().iterator();i.hasNext();) {
      Map.Entry<URI, byte[]> me = i.next();
      uris[idx] = me.getKey().toASCIIString();
      content[idx] = me.getValue();
      ++idx;
    }

    LSInputList list = new LSInputList() {
      public LSInput item(int index) {
        DOMInputImpl input = new DOMInputImpl();
        input.setSystemId(uris[index]);
        input.setByteStream(new ByteArrayInputStream(content[index]));
        return input;
      }

      public int getLength() {
        return uris.length;
      }
    };

    return new SchemaModelImpl(schemaLoader.loadInputList(list));
  }

  /**
   * @see com.fs.utils.xsd.SchemaModel#isCompatible(javax.xml.namespace.QName,
   *      javax.xml.namespace.QName)
   */
  public boolean isCompatible(QName type1, QName type2) {
    XSTypeDefinition typeDef1;
    XSTypeDefinition typeDef2;

    if (knowsElementType(type1)) {
      typeDef1 = _model.getElementDeclaration(type1.getLocalPart(),
                                              type1.getNamespaceURI())
                       .getTypeDefinition();
    } else if (knowsSchemaType(type1)) {
      typeDef1 = _model.getTypeDefinition(type1.getLocalPart(),
                                          type1.getNamespaceURI());
    } else {
      throw new IllegalArgumentException("unknown schema type: " + type1);
    }

    if (knowsElementType(type2)) {
      typeDef2 = _model.getElementDeclaration(type2.getLocalPart(),
                                              type2.getNamespaceURI())
                       .getTypeDefinition();
    } else if (knowsSchemaType(type2)) {
      typeDef2 = _model.getTypeDefinition(type2.getLocalPart(),
                                          type2.getNamespaceURI());
    } else {
      throw new IllegalArgumentException("unknown schema type: " + type2);
    }

    return typeDef1.derivedFromType(typeDef2, (short)0)
           || typeDef2.derivedFromType(typeDef1, (short)0);
  }

  /**
   * @see com.fs.utils.xsd.SchemaModel#isSimpleType(javax.xml.namespace.QName)
   */
  public boolean isSimpleType(QName type) {
    if (type == null)
      throw new NullPointerException("Null type argument!");

    XSTypeDefinition typeDef = _model.getTypeDefinition(type.getLocalPart(),
                                                        type.getNamespaceURI());

    return (typeDef != null)
           && (typeDef.getTypeCategory() == XSTypeDefinition.SIMPLE_TYPE);
  }

  /**
   * @see com.fs.utils.xsd.SchemaModel#knowsElementType(javax.xml.namespace.QName)
   */
  public boolean knowsElementType(QName elementType) {
    if (elementType == null)
      throw new NullPointerException("Null type argument!");

    return _model.getElementDeclaration(elementType.getLocalPart(),
                                        elementType.getNamespaceURI()) != null;
  }

  /**
   * @see com.fs.utils.xsd.SchemaModel#knowsSchemaType(javax.xml.namespace.QName)
   */
  public boolean knowsSchemaType(QName schemaType) {
    if (schemaType == null)
      throw new NullPointerException("Null type argument!");

    return _model.getTypeDefinition(schemaType.getLocalPart(),
                                    schemaType.getNamespaceURI()) != null;
  }
}
