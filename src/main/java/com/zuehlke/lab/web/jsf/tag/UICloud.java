package com.zuehlke.lab.web.jsf.tag;


import java.io.IOException;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import com.zuehlke.lab.web.jsf.model.Cloud;

@FacesComponent(value = "cloud")
public class UICloud extends UIComponentBase{
    
        private int width;
        private int height;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    
    @Override
    public void encodeBegin(FacesContext context) throws IOException {


    }

    
        @Override
       public void encodeEnd(FacesContext context) throws IOException {
   
           ResponseWriter responseWriter = context.getResponseWriter();
           
           writeScriptTag(responseWriter);
           
           Cloud model = (Cloud)getAttributes().get("model");
           
           writeCanvas(responseWriter,context, model);
           writeScript(responseWriter,getClientId(context),model.getId(),model.getLabel());
       }
        
       private void writeScriptTag(ResponseWriter writer) throws IOException{
           writer.startElement("script", null);
           writer.writeAttribute("src", "tagcanvas.js", null);
           writer.writeAttribute("type", "text/javascript", null);
           writer.endElement("script");
       }
       
       private void writeScript(ResponseWriter writer,String id, int cloudId, String cloudLable) throws IOException{
          writer.startElement("script", null);
          writer.writeAttribute("type", "text/javascript", null);
          writer.write("\nvar gradient = {\n");
          writer.write("0:   'rgb(0,100,0)',  \n");
          writer.write("0.33: 'rgb(154,205,50)',\n");
          writer.write("0.66: 'rgb(49,79,79)',  \n");
          writer.write("1:   'rgb(200,200,200)' \n");
          writer.write("};\n");
          writer.write("var path = new Array(new Array('"+cloudId+"','"+cloudLable+"'));");
          
          writer.write("window.onload = function() {\n");
          writer.write("TagCanvas.weightGradient = gradient;\n");
          writer.write("TagCanvas.textFont = 'Trebuchet MS, Helvetica, sans-serif';\n");
          writer.write("TagCanvas.textColour = '#00f';\n");
          writer.write("TagCanvas.textHeight = 25;\n");
          writer.write("TagCanvas.outlineColour = '#ffffff';\n");
          writer.write("TagCanvas.outlineThickness = 0;\n");
          //writer.write("TagCanvas.freezeActive = true;\n");
          writer.write("TagCanvas.maxSpeed = 0.03;\n");
          writer.write("TagCanvas.minBrightness = 0.2;\n");
	  writer.write("TagCanvas.depth = 0.92;\n");
	  writer.write("TagCanvas.pulsateTo = 0.6;\n");
	  writer.write("TagCanvas.initial = [0.1,-0.1];\n");
	  writer.write("TagCanvas.decel = 0.98;\n");
	  writer.write("TagCanvas.reverse = true;\n");
	  writer.write("TagCanvas.hideTags = false;\n");
	  writer.write("TagCanvas.shadow = '#ccf';\n");
	  writer.write("TagCanvas.shadowBlur = 3;\n");
	  writer.write("TagCanvas.weight = true;\n");
          writer.write("TagCanvas.weightFrom = 'data-weight';\n");
          writer.write("TagCanvas.weightMode = 'both';\n");
          writer.write("try{\n");
          writer.write("TagCanvas.Start('"+id+"','tags_"+cloudId+"');\n");
          writer.write("} catch(e) {// something went wrong, hide the canvas container\n document.getElementById('"+id+"').style.display = 'none';}\n");
          writer.write("};\n");
          writer.write("function switchCloud(id,label) {TagCanvas.Start('"+id+"','tags_'+id); document.getElementById('title_"+id+"').innerHTML = label; changePath(id,label); return false;}\n");
          writer.write("function changePath(id,label) { var found = (-1); for (var i = 0; i < path.length; ++i){if(path[i][0] == id){found = i}}\n");
          writer.write("if(found == (-1)){path.push(new Array(id,label))}\n");
          writer.write("else{path = path.slice(0,found+1)}\n");
          writer.write("var path_content = 'Path: '; for(var i = 0; i < path.length-1; ++i){\n");
          writer.write("path_content = path_content.concat(' &gt;&gt; <a class=\"path_link\" href=\"#\"onClick=\"switchCloud(').concat('path['+i+'][0],path['+i+'][1]').concat(')\">').concat(path[i][1]).concat('</a> ');\n");
          writer.write("}\n");
          writer.write("document.getElementById('path_"+id+"').innerHTML = path_content");
          writer.write("};\n");
          
          writer.endElement("script");
       }

    private void writeCanvas(ResponseWriter writer, FacesContext context,Cloud cloud) throws IOException{
        writer.startElement("div", null);
        writer.writeAttribute("id","title_"+getClientId(context),null);
        writer.write(cloud.getLabel());
        writer.endElement("div");
        writer.startElement("div", null);
        writer.writeAttribute("id","size_"+getClientId(context),null);
        writer.startElement("canvas", null);
        writer.writeAttribute("id",getClientId(context),"id");
        writer.writeAttribute("name", getClientId(context),"clientId");
        //writer.writeAttribute("width", "100%", null);//getAttributes().get("width"), null);
        //writer.writeAttribute("height", "100%", null);//getAttributes().get("height"), null);
        writer.endElement("canvas");
        writer.endElement("div");
        writer.startElement("div", null);
        writer.writeAttribute("style", "display: none", null);
        writeCloud(writer, cloud);
        writer.endElement("div");
        writer.startElement("div", null);
        
        writer.writeAttribute("id","path_"+getClientId(context),"id");
        writer.write("awdasdasdawdasdasdawdsa");
        writer.endElement("div");
    }
    
    private void writeCloud(ResponseWriter writer, Cloud cloud) throws IOException{
        if(cloud.hasSubCloud()){
            writer.startElement("ul", null);
            writer.writeAttribute("id", "tags_"+cloud.getId(), null);
            for(Cloud element : cloud.getElements())
            {
                writeListElement(writer,element);
            }
            writer.endElement("ul");
            
            for(Cloud element : cloud.getElements())
            {
                writeCloud(writer, element);
            }
        }
    }
    
    private void writeListElement(ResponseWriter writer, Cloud cloud) throws IOException{
        writer.startElement("li", null);
        writer.startElement("a", null);
        writer.writeAttribute("href", "#", null);
        if(cloud.hasSubCloud()){
            writer.writeAttribute("onclick", "return switchCloud('"+cloud.getId()+"','"+cloud.getLabel()+"')", null);
        }
        writer.writeAttribute("data-weight", (cloud.getWeight()*3+10), null);
        writer.write(cloud.getLabel());
        writer.endElement("li");
    }

    @Override
    public String getFamily() {
       return "family";
    }
  }