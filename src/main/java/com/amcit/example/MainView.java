package com.amcit.example;

import java.awt.Color;
import java.util.HashMap;
import java.util.UUID;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.AbstractConfigurationObject;
import com.vaadin.flow.component.charts.model.AbstractPlotOptions;
import com.vaadin.flow.component.charts.model.AreaOptions;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.PlotBand;
import com.vaadin.flow.component.charts.model.PlotLine;
import com.vaadin.flow.component.charts.model.PlotOptionsBar;
import com.vaadin.flow.component.charts.model.PlotOptionsColumn;
import com.vaadin.flow.component.charts.model.PlotOptionsColumnrange;
import com.vaadin.flow.component.charts.model.PlotOptionsPie;
import com.vaadin.flow.component.charts.model.PointOptions;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.Route;


@HtmlImport( "styles/shared-styles.html" )
@Route( "" )
public class MainView extends Div
{

    private HashMap<AbstractConfigurationObject, String> map = new HashMap<>();
    // this element contains all stylings for the chart
    Element style = new Element( "style" );
    private String styleId = "membrane";
    
    public MainView()
    {
        setId("semipermeable");
        style.setAttribute( "id", styleId );
        Chart chart = new Chart( ChartType.BAR );
        DataSeries series = new DataSeries();

        series.setPlotOptions( new PlotOptionsBar() );
        series.add( new DataSeriesItem( 0, 2 ) );
        DataSeriesItem item = new DataSeriesItem();
        item.setX( 1 );
        item.setY( 5 );
        series.add( item );

        PlotBand band = new PlotBand( 3, 5 );
        chart.getConfiguration()
                .getyAxis()
                .addPlotBand( band );

        PlotLine line = new PlotLine( 2 );
        line.setZIndex( 100 );
        chart.getConfiguration()
                .getyAxis()
                .addPlotLine( line );

        // dynamic styling via CSS
        setColorForItem( item, Color.red );
        setColorForPlotBand( band, Color.blue );
        setColorForPlotLine( line, Color.green );
        setWidthForPlotLine( line, 5);
        setDashStyle( line, DashStyle.SHORTDASHDOT );
        setColorForPlotOptions( series.getPlotOptions(), Color.MAGENTA );
        
        chart.getConfiguration().addSeries(series);
        getElement().appendChild( style );
        add( chart );
        
        // This moves the style-Element below the shadow-root of the chart.
        UI.getCurrent().getPage().executeJavaScript("document.getElementById(\"" + getId().get() + "\").getElementsByTagName(\"vaadin-chart\")[0].shadowRoot.appendChild(document.getElementById(\"" + styleId + "\"))" );
    }
    
    public void setColorForPlotOptions(AbstractPlotOptions aPlotOptions, Color aColor) {
        String className = getID( aPlotOptions );
        setClassNameIfMatches( aPlotOptions, className );
        
        StringBuilder str = new StringBuilder();
        String hexString = getHexString( aColor );

        str.append( ".highcharts-series." + className + " .highcharts-point" );
        str.append( '{' );
        str.append( "fill: " + hexString + ';');
        str.append( "stroke: " + hexString + ';');
        str.append( "} " );
        
        addCSS(str.toString());
    }
    
    public void setDashStyle(PlotLine aPlotLine, DashStyle aDashStyle )
    {
        String id = getID( aPlotLine );
        aPlotLine.setClassName( id );
        
        String pattern = getPattern( aDashStyle );
        
        StringBuilder css = new StringBuilder();
        css.append( ".highcharts-plot-line." + id );
        css.append( "{" );
        css.append( "stroke-dasharray: " + pattern + ";" );
        css.append( "}" );
        addCSS( css.toString() );
    }

    private void setWidthForPlotLine( PlotLine aPlotLine, int aWidth )
    {
        String className = getID( aPlotLine );
        aPlotLine.setClassName( className );
        
        StringBuilder css = new StringBuilder();
        css.append( ".highcharts-plot-line." + className );
        css.append( "{" );
        css.append( "stroke-width: " + aWidth + ";");
        css.append( "}" );
        
        addCSS( css.toString() );
        
    }

    private void setColorForPlotLine( PlotLine aPlotLine, Color aColor)
    {
        String className = getID( aPlotLine );

        aPlotLine.setClassName( className );
        String hexString = getHexString( aColor );

        StringBuilder str = new StringBuilder();

        str.append( "path.highcharts-plot-line." + className );
        str.append( '{' );
        str.append( "stroke: " + hexString + ';' );
        str.append( "} " );

        addCSS( str.toString() );
    }

    private void setColorForPlotBand( PlotBand aPlotBand, Color aColor )
    {
        String className = getID( aPlotBand );

        aPlotBand.setClassName( className );
        String hexString = getHexString( aColor );

        StringBuilder str = new StringBuilder();

        str.append( ".highcharts-plot-band." + className );
        str.append( '{' );
        str.append( "fill: " + hexString + ';' );
        str.append( "} " );

        addCSS( str.toString() );

    }

    private void setColorForItem( DataSeriesItem aItem, Color aColor )
    {
        String className = getID( aItem );
        aItem.setClassName( className );
        aItem.setId( className );

        StringBuilder str = new StringBuilder();

        String hexString = getHexString( aColor );

        str.append( ".highcharts-point." + className );
        str.append( '{' );
        str.append( "fill: " + hexString + " !important;" );
        str.append( "stroke: " + hexString + " !important;" );
        str.append( "stroke-width: 2;" );
        str.append( "} " );

        addCSS( str.toString() );
    }

    private void addCSS( String aRule )
    {
        style.setText( style.getText() + aRule );
    }
    
    private String getID( AbstractConfigurationObject aConfigurationObject )
    {
        String id = map.get( aConfigurationObject );
        if( id == null ) {
            id = "AMC" + UUID.randomUUID()
                    .toString();
            map.put( aConfigurationObject, id );
        }
        return id;
    }

    public static String getHexString( Color aColor )
    {
        return String.format( "#%02x%02x%02x", aColor.getRed(), aColor.getGreen(), aColor.getBlue() )
                .toUpperCase();
    }
    
    private void setClassNameIfMatches( AbstractPlotOptions aPlotOptions, String className )
    {
        if(aPlotOptions instanceof AreaOptions) {
            ((AreaOptions) aPlotOptions).setClassName( className );
        }
        else if(aPlotOptions instanceof PlotOptionsBar) {
            ((PlotOptionsBar) aPlotOptions).setClassName( className );
        }
        else if(aPlotOptions instanceof PlotOptionsColumn) {
            ((PlotOptionsColumn) aPlotOptions).setClassName( className );
        }
        else if(aPlotOptions instanceof PlotOptionsColumnrange) {
            ((PlotOptionsColumnrange) aPlotOptions).setClassName( className );
        }
        else if( aPlotOptions instanceof PointOptions) {
            ((PointOptions) aPlotOptions).setClassName( className );
        }
        else if( aPlotOptions instanceof PlotOptionsPie ) {
            ((PlotOptionsPie) aPlotOptions).setClassName( className );
        }
    }
    
    private String getPattern( DashStyle aDashStyle )
    {
        String pattern;
        switch(aDashStyle) {
        case DASH:
            pattern = "8,6";
            break;
        case DASHDOT:
            pattern = "8,6,2,6";
            break;
        case DOT:
            pattern = "2,6";
            break;
        case LONGDASH:
            pattern = "16,6";
            break;
        case LONGDASHDOT:
            pattern = "16,6,2,6";
            break;
        case LONGDASHDOTDOT:
            pattern = "16,6,2,6,2,6";
            break;
        case SHORTDASH:
            pattern = "6,2";
            break;
        case SHORTDASHDOT:
            pattern = "6,2,2,2";
            break;
        case SHORTDASHDOTDOT:
            pattern = "6,2,2,2,2,2";
            break;
        case SHORTDOT:
            pattern = "2,2";
            break;
        case SOLID:
            pattern = "none";
            break;
        default:
            pattern = "none";
            break;
            
        }
        return pattern;
    }
}
