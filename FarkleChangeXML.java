import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FarkleChangeXML
{
    private Document dom;

    private final String OUTPUT_FILENAME = "farkle.xml";

    private final String PLAYERS_TAG_NAME = "players";
    private final String HUMAN_TAG_NAME = "human";
    private final String CPU_TAG_NAME = "cpu";
    private final String NAME_TAG_NAME = "name";
    private final String TURNS_TAG_NAME = "turns";
    private final String TURN_TAG_NAME = "turn";
    private final String TURNSCORE_TAG_NAME = "turnScore";
    private final String SCORE_TAG_NAME = "score";

    // purpose: constructor for FarkleChangeXML object
    // pre-conditions: game has not yet started
    // post-conditions: Document dom is created out of given xml file
    public FarkleChangeXML()
    {
        dom = createDOM(OUTPUT_FILENAME);
    }

    //*******************************************************************
    //test class
    public void go()
    {
        if (dom == null)
            System.out.println("Error creating XML DOM. Program terminating.");
        else
        {
            initXML();

            setName("cpu","gary");
            setName("human","ash");
            addTurn("human", 200, 500);
            addTurn("human", 200, 500);
            addTurn("human", 200, 500);

            addTurn("cpu", 200, 500);
            addTurn("cpu", 200, 500);
            addTurn("cpu", 200, 500);

            initXML();
            saveDOMtoXML();
        }

    }
    ////*******************************************************************


    // purpose: creates a dom file from a given xml file
    // pre-conditions: xml file exists
    // post-conditions: dom is created based on xml file
    private Document createDOM(String filename)
    {
        Document dom;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try
        {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse(filename);
        }
        catch (ParserConfigurationException ex)
        {
            System.out.println(ex);
            dom = null;
        }
        catch (IOException ex)
        {
            System.out.println(ex);
            dom = null;
        }
        catch (SAXException ex)
        {
            System.out.println(ex);
            dom = null;
        }
        catch (Exception ex)
        {
            System.out.println(ex);
            dom = null;
        }
        return dom;
    }

    // purpose: resets all relevant values in xml persistent storage file
    // pre-conditions: game is starting (or game has ended)
    // post-conditions: xml file is reset and ready for a new game
    public void initXML()
    {
        setName("human", "");
        setName("cpu", "");

        resetTurns("human");
        resetTurns("cpu");

        saveDOMtoXML();
//        resetTotalScore("cpu");
//        resetTotalScore("human");

//        resetTurnScore("cpu");
//        resetTurnScore("human");
    }

    // purpose: deletes all turn nodes of "player"
    // pre-conditions: game has started, want to clear turns
    // post-conditions: turns are deleted (child nodes of "turns" tag are deleted)
    public void resetTurns(String player)
    {
        Element eTurns = findTurnsElement(dom.getDocumentElement(), player);
//        System.out.println(((Element)eTurns.getParentNode()).getTagName());

//        Node nTurns = (Node) eTurns;
        while (eTurns.hasChildNodes())
        {
            // saved to Node x for testing
            Node x = eTurns.removeChild(eTurns.getFirstChild());
//            System.out.println("removed node" + x.getNodeName());
        }

        saveDOMtoXML();
    }

    // purpose: adds a turn element to xml doc
    // pre-conditions: player/cpu has finished a turn
    // post-conditions: turn is added to the appropriate player including:
    //      - turnScore
    //      - score
    // tags.
    public void addTurn(String player, Integer turnScore, Integer totalScore)
    {
        Element docElement = dom.getDocumentElement();  //gives root node ("players")

        Element eTurn = dom.createElement(TURN_TAG_NAME);
        Element eTurnScore = dom.createElement(TURNSCORE_TAG_NAME);
        Element eScore = dom.createElement(SCORE_TAG_NAME);

        eTurnScore.setTextContent(turnScore.toString());
        eScore.setTextContent(totalScore.toString());

        eTurn.appendChild(eTurnScore);
        eTurn.appendChild(eScore);


        Element eTurns = findTurnsElement(dom.getDocumentElement(), player);
        eTurns.appendChild(eTurn);

        saveDOMtoXML();
    }

    // purpose: sets the name of some player
    // pre-conditions: game has just started, namePrompt called
    // post-conditions: player's name is changed to input param name
    public void setName(String player, String name)
    {
        Element docElement = dom.getDocumentElement();  //gives root node ("players")

        //gives the score element for the player specified
        Element nameElement = findNameElement(docElement, player);

        if (nameElement == null)
            System.out.println("score not in XML DOM.");
        else
        {
            //System.out.println("Found score in XML DOM.");

            nameElement.setTextContent(name);
            //System.out.println(nameElement.getTextContent());
        }

        saveDOMtoXML();
    }

    // purpose: finds the turns element of the player in the DOM file
    // pre-conditions: some method wants to find the turns element of player
    // post-conditions: returns player's turns element
    private Element findTurnsElement(Element docElement, String player)
    {
        NodeList playersNodes = dom.getElementsByTagName(PLAYERS_TAG_NAME);
        Element ePlayers = (Element) playersNodes.item(0);
        NodeList playerNodes = ePlayers.getElementsByTagName(player);
        Element ePlayer = (Element) playerNodes.item(0);
        NodeList turnsNodes = ePlayer.getElementsByTagName(TURNS_TAG_NAME);
        Element eTurns = (Element) turnsNodes.item(0);

        return eTurns;
    }

    // purpose: finds the name element of the player in the DOM file
    // pre-conditions: some method wants to find the name element of player
    // post-conditions: returns player's name element
    private Element findNameElement(Element docElement, String player)
    {
        NodeList playersNodes = dom.getElementsByTagName(PLAYERS_TAG_NAME);

        Element ePlayers = (Element) playersNodes.item(0);
        NodeList playerNodes = ePlayers.getElementsByTagName(player);
        Element ePlayer = (Element) playerNodes.item(0);
        NodeList scoreNodes = ePlayer.getElementsByTagName(NAME_TAG_NAME);
        Element eName = (Element) scoreNodes.item(0);

        //verify it's at the correct spot in the xml file
        /*
        //System.out.println(player);
        //System.out.println(ePlayers.getNodeName());
        //System.out.println(ePlayer.getNodeName());
        //System.out.println(eScore.getNodeName());
        //System.out.println(eScore.getTextContent());
        */
        return eName;
    }

    // purpose: save DOM changes to XML file
    // pre-conditions: DOM has been updated.
    // post-conditions: Save DOM to a new XML file name.
    private void saveDOMtoXML()
    {
        try
        {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();

            /* NEW: formatting options */
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            // doesn't format indents perfectly because
            //  file is already created.
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(dom);
            StreamResult result = new StreamResult(new File(OUTPUT_FILENAME));
            transformer.transform(source, result);
        }
        catch (TransformerFactoryConfigurationError ex)
        {
        }
        catch (TransformerConfigurationException ex)
        {
        }
        catch (TransformerException ex)
        {
        }
    }
}
