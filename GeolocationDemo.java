import net.rim.device.api.gps.*;
import net.rim.device.api.system.Application;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;
import javax.microedition.location.*;


public final class GeolocationDemo extends UiApplication
{
    public static void main(String[] args)
    {
        GeolocationDemo theApp = new GeolocationDemo();
        theApp.enterEventDispatcher();
    }
    
    public GeolocationDemo() 
    {
        pushScreen(new GeolocationScreen());
    }
}

class GeolocationScreen extends MainScreen 
{
    private LabelField _coordLabel;
    private double _latitude;
    private double _longitude;
          
    public GeolocationScreen() 
    {
        super(DEFAULT_CLOSE | DEFAULT_MENU);

        setTitle(new LabelField("Geolocation Demo" , Field.USE_ALL_WIDTH | 
                DrawStyle.HCENTER));

    
        ButtonField geolocButton = new ButtonField("Get geolocation", 
                ButtonField.CONSUME_CLICK);
        geolocButton.setChangeListener(new FieldChangeListener()
        {
            public void fieldChanged(Field field, int context)
            {
                findGeolocation();
            }
        });
        add(geolocButton);
        
        this._coordLabel = new LabelField();
        add(this._coordLabel);
    }

    
    private void findGeolocation()
    {
        this._coordLabel.setText("");
               
        Thread geolocThread = new Thread() 
        {
            public void run() 
            {
                try
                {
                    BlackBerryCriteria myCriteria = new BlackBerryCriteria(
                            LocationInfo.GEOLOCATION_MODE);


                    try
                    {
                        BlackBerryLocationProvider myProvider = (
                                BlackBerryLocationProvider)LocationProvider
                                .getInstance(myCriteria);


                        try
                        {
                            BlackBerryLocation myLocation = (BlackBerryLocation)
                                    myProvider.getLocation(300);
                            _longitude = myLocation.getQualifiedCoordinates()
                                    .getLongitude();
                            _latitude = myLocation.getQualifiedCoordinates()
                                    .getLatitude();
                            
                            StringBuffer sb = new StringBuffer();
                            sb.append("Longitude: ");
                            sb.append(_longitude);
                            sb.append("\n");
                            sb.append("Latitude: ");
                            sb.append(_latitude);
                            String msg = sb.toString();
                            showResults(msg);
                        }
                        catch (InterruptedException e)
                        {
                            showException(e);
                        }
                        catch (LocationException e)
                        {
                            showException(e);
                        }
                    }
                    catch (LocationException e)
                    {
                        showException(e);
                    }
               } 
               catch (UnsupportedOperationException e) 
               {
                   showException(e);
               }
            }
        };
        geolocThread.start();
    }


    private void showResults(final String msg)
    {
        Application.getApplication().invokeLater(new Runnable()
        {
            public void run()
            {
                GeolocationScreen.this._coordLabel.setText(msg);
            }
        });
    }

   
    private void showException(final Exception e) 
    {
        Application.getApplication().invokeLater(new Runnable() 
        {
            public void run() 
            {
                Dialog.alert(e.getMessage());
            }
        });
    }
}
