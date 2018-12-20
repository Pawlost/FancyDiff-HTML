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

package org.pawlost.work.html.compare;

import org.jsoup.nodes.Document;
import org.pawlost.work.html.IO.Connector;
import org.pawlost.work.html.elements.HighterElement;
import org.pawlost.work.html.elements.LesserElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class NormalCompare implements Compare {

    private HighterElement highterElement;
    private LesserElement lesserElement;
    private String tempPath;

    public NormalCompare(HashMap<Integer, Document> comparedSources, String path){
        this.comparedSources = comparedSources;
        tempPath = path;
    }

    public void start() {
       System.out.println("Normal compare");
        HashMap<Integer, Document> oldPDFChapters = comparedSources.get("old");
        HashMap<Integer, Document> newPDFChapters = comparedSources.get("new");
        if (oldPDFChapters.size() < newPDFChapters.size()) {
            ArrayList<Integer> keys = (ArrayList<Integer>) new ArrayList(newPDFChapters.keySet());
            for (int i : keys) {
                if (i > oldPDFChapters.size()) {
                    try {
                        Connector.createFile(tempPath + "created" + i + ".html", newPDFChapters.get(i).html());
                        newPDFChapters.remove(i);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (oldPDFChapters.size() > newPDFChapters.size()) {
            ArrayList<Integer> keys = (ArrayList<Integer>) new ArrayList(oldPDFChapters.keySet());
            for (int i : keys) {
                if (i > newPDFChapters.size()) {
                    try {
                        Connector.createFile(tempPath + "removed" + i + ".html", oldPDFChapters.get(i).html());
                        oldPDFChapters.remove(i);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("Normal compare done");
        System.out.println("Chapters remain to compare: " + oldPDFChapters.size() + " old chapter and " + newPDFChapters.size()
                + " new chapter \n");
        comparedSources = new HashMap<>();
        comparedSources.put("new", newPDFChapters);
        comparedSources.put("old", oldPDFChapters);
    }
}
