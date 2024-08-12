package kubernetes.build.service.type;

import datasource.work.model.entity.WorkProjectSetting;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class MavenProjectImageService {
    private Element getOrCreateElement(Document doc, String tagName) {
        NodeList elements = doc.getElementsByTagName(tagName);
        if (elements.getLength() > 0) {
            return (Element) elements.item(0);
        } else {
            Element newElement = doc.createElement(tagName);
            doc.getDocumentElement().appendChild(newElement);
            return newElement;
        }
    }

    private Element getOrCreateElement(Element parent, String tagName) {
        NodeList elements = parent.getElementsByTagName(tagName);
        if (elements.getLength() > 0) {
            return (Element) elements.item(0);
        } else {
            Element newElement = parent.getOwnerDocument().createElement(tagName);
            parent.appendChild(newElement);
            return newElement;
        }
    }

    private Element  createJibConfigurationElement(Document pomDoc) {
        Element configurationElement = pomDoc.createElement("configuration");

        Element fromElement = pomDoc.createElement("from");
        Element imageElement = pomDoc.createElement("image");
        imageElement.appendChild(pomDoc.createTextNode("openjdk:11-jre-alpine"));
        fromElement.appendChild(imageElement);

        configurationElement.appendChild(fromElement);

        return configurationElement;
    }

    private Element  createJibPluginElement(Document pomDoc) {
        Element pluginElement = pomDoc.createElement("plugin");

        Element groupIdElement = pomDoc.createElement("groupId");
        groupIdElement.appendChild(pomDoc.createTextNode("com.google.cloud.tools"));

        Element artifactIdElement = pomDoc.createElement("artifactId");
        artifactIdElement.appendChild(pomDoc.createTextNode("jib-maven-plugin"));

        Element versionElement = pomDoc.createElement("version");
        versionElement.appendChild(pomDoc.createTextNode("3.2.0"));

        Element configurationElement = createJibConfigurationElement(pomDoc);

        pluginElement.appendChild(groupIdElement);
        pluginElement.appendChild(artifactIdElement);
        pluginElement.appendChild(versionElement);
        pluginElement.appendChild(configurationElement);

        return pluginElement;
    }

    public void jibSetting(String projectPath, WorkProjectSetting projectSetting) {
        Path pomFilePath = Paths.get(projectPath, "pom.xml");
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        Document doc = null;

        try {
            docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.parse(Files.newInputStream(pomFilePath));

            Element buildElement = getOrCreateElement(doc, "build");
            Element pluginsElement = getOrCreateElement(buildElement, "plugins");

            Element pluginElement = createJibPluginElement(doc);
            pluginsElement.appendChild(pluginElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(Files.newOutputStream(pomFilePath, StandardOpenOption.WRITE));
            transformer.transform(source, result);

        } catch (ParserConfigurationException | IOException | TransformerException | SAXException e) {
            throw new RuntimeException(e);
        }
    }
}
