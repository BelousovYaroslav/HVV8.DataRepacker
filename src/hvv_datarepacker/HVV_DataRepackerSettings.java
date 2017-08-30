/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_datarepacker;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *
 * @author yaroslav
 */
public class HVV_DataRepackerSettings {
    static Logger logger = Logger.getLogger(HVV_DataRepackerSettings.class);
    
    private int m_nTimeZoneShift;
    public int GetTimeZoneShift() { return m_nTimeZoneShift;}
    
    private int m_nPollerSingleInstanceSocketServerPort;
    public int GetPollerSingleInstanceSocketServerPort() { return m_nPollerSingleInstanceSocketServerPort;}
    
    private int m_nArcViewerSingleInstanceSocketServerPort;
    public int GetArcViewerSingleInstanceSocketServerPort() { return m_nArcViewerSingleInstanceSocketServerPort;}
    
    public HVV_DataRepackerSettings( String strAMSRoot) {
        m_nTimeZoneShift = 0;
        
        m_nPollerSingleInstanceSocketServerPort = 10000;
        m_nArcViewerSingleInstanceSocketServerPort = 10005;
        
        ReadSettings();
    }
    
    private boolean ReadSettings() {
        boolean bResOk = true;
        try {
            SAXReader reader = new SAXReader();
            
            String strSettingsFilePathName = System.getenv( "AMS_ROOT") + "/etc/settings.data.repacker.xml";
            URL url = ( new java.io.File( strSettingsFilePathName)).toURI().toURL();
            
            Document document = reader.read( url);
            
            Element root = document.getRootElement();

            // iterate through child elements of root
            for ( Iterator i = root.elementIterator(); i.hasNext(); ) {
                Element element = ( Element) i.next();
                String name = element.getName();
                String value = element.getText();
                
                if( "timezone".equals( name)) m_nTimeZoneShift = Integer.parseInt( value);
                
                if( "singleInstancePort.ArcViewer".equals( name)) m_nArcViewerSingleInstanceSocketServerPort = Integer.parseInt( value);
                if( "singleInstancePort.Poller".equals( name)) m_nPollerSingleInstanceSocketServerPort = Integer.parseInt( value);
            }
            
        } catch( MalformedURLException ex) {
            logger.error( "MalformedURLException caught while loading settings!", ex);
            bResOk = false;
        } catch( DocumentException ex) {
            logger.error( "DocumentException caught while loading settings!", ex);
            bResOk = false;
        }
        
        return bResOk;
    }
}
