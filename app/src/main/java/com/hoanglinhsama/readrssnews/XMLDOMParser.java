package com.hoanglinhsama.readrssnews;

import android.util.Log; // Kiem tra MockLog de kiem tra khong phai tren may chu android

import org.w3c.dom.Document; // Document la tai lieu dai dien cho toan bo tai lieu HTML hoac tai lieu XML
import org.w3c.dom.Element; // Element dai dien cho toan bo element trong document HTML hoac document XML, Elemnet  ke thua tu Node
import org.w3c.dom.Node; // Node la kieu du lieu chinh cho toan bo DOM document, no dai dien cho 1 node duy nhat trong document tree
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource; // InputSource la mot nguon dau vao duy nhat cho mot thuc the XML
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader; // StringReader la mot luong ky tu co nguon la mot chuoi

import javax.xml.parsers.DocumentBuilder; // DocumentBuilder xac dinh API dung de lay cac instance cua Document DOM tu XML Document
import javax.xml.parsers.DocumentBuilderFactory; // DocumentBuilderFactory xac dinh API cho phep ung dung tao ra tree DOM object tu XML Documnet (cung co the hieu la dung de tao ra mot instance cua DocumentBuilder)
import javax.xml.parsers.ParserConfigurationException;

public class XMLDOMParser { // class tu viet de doc (sua, xoa) XML truc quan va de hon
    public Document getDocument(String xml) {
        Document document = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // tao mot DocumentBuilderFactory moi de co the tao ra mot DocumentBuilder
        try {
            DocumentBuilder db = factory.newDocumentBuilder(); // tao doi tuong documentbuilder de co the lay duoc document tu XML va chuyen ve dang DOM de de doc
            InputSource is = new InputSource(); // tao mot InputSource la nguon dau vao duy nhat cho mot thuc the XML (de doc du lieu tu XML document)
            is.setCharacterStream(new StringReader(xml)); // dat luong character vao cho input source nay (tham so la luong character chua XML document)
            is.setEncoding("UTF-8"); // dat kieu encoding cho character la UTF-8, UTF-8 la loai encoding de dien dat Unicode tren bo nho
            document = db.parse(is); // tao document (parse de parse noi dung cua source input (tu XML document ve DOM de de doc)
        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage(), e);
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage(), e);
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage(), e);
            return null;
        }
        return document;
    }

    public String getValue(Element item, String name) {
        NodeList nodes = item.getElementsByTagName(name); // tra ve mot cai NodeList (nodes) cua tat ca cac element con ( Element item ) theo name cua element
        return this.getTextNodeValue(nodes.item(0)); // tra ve du lieu cua cai node thu 0 cua NodeList
    }

    private String getTextNodeValue(Node elem) {
        Node child;
        if (elem != null) { // neu node co content
            if (elem.hasChildNodes()) { // neu node co chua node con ben trong no
                for (child = elem.getFirstChild(); child != null; child = child.getNextSibling()) { // duyet tung node con cua node elem cho den khi khong con node con (child ==null), FirstChild la node con dau tien cua no, NextSibling la node con ke tiep
                    if (child.getNodeType() == Node.TEXT_NODE) { // ney node dang duyet la TEXT node (tuc la node chua text)
                        return child.getNodeValue(); // tra ve noi dung cua node
                    }
                }
            }
        }
        return ""; // neu null tuc la cai node thu 0 khong co content, do do tra ve mot chuoi rong
    }
}


