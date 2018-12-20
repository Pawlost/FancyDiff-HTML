/*  This file is part of FancyDiff-HTML.

    FancyDiff-HTML is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FancyDiff-HTML is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with FancyDiff-HTML.  If not, see <https://www.gnu.org/licenses/>.
    */
package org.pawlost.work.html.elements;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

 public class LesserElement extends ArrayList<ArrayList<Element>> {

     public ArrayList<Element> children;
     public Element mainElement;

     public LesserElement(Document diff) {
         children = new ArrayList<>();
         children.add(diff.body());
         addChildren();
         mainElement = getFirstElement();
     }

     public LesserElement() {
         children = new ArrayList<>();
     }

     public void appendChange(String text) {
         mainElement.append("<font class='FancyDiff' color='red'>" + text + "</font>");
     }

     public void removeAllLast(){
         while(!getLastChildren().isEmpty()){
            getLastChildren().remove(0);
         }
     }

     public void changeMainElement() {
         mainElement = getMainChild(0);
     }

     public void changeMainElement(Element change) {
        mainElement = change;
     }

     public Element getFirstElement(){
         return get(0).get(0);
     }

     public void addChildren() {
         super.add((ArrayList<Element>) children.clone());
         children = new ArrayList<>();
     }

     public void addChildren(Element child) {
         children.add(child);
     }

     public int getMainESize() {
         return mainElement.children().size();
     }

     public Element getMainChild(int index) {
         return mainElement.child(index);
     }

     public ArrayList<Element> getLastChildren() {
         return get(size() - 1);
     }

     public LesserElement cloneE() {
         LesserElement less = new LesserElement();
         for(ArrayList<Element> child:this){
             less.add((ArrayList<Element>) child.clone());
         }
         less.mainElement = this.mainElement;
         return less;
     }
 }
