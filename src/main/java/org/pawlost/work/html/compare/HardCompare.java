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

import java.io.File;
import java.util.HashMap;

//hard compare, compares whole pages
public class HardCompare implements Compare{
    public HardCompare() {

    }

    @Override
    public void start() {
            System.out.println("Starting Hard Compare");
            int size = (oldPDFChapters.size() <= newPDFChapters.size() ? oldPDFChapters.size() : newPDFChapters.size());
            File dir = new File(tempFolder);
            dir.mkdirs();
            for (int i = 1; i <= size; i++) {
                String htmlOldString = oldPDFChapters.get(i).html();
                String htmlNewString = newPDFChapters.get(i).html();

                htmlNewString = htmlNewString.replace(" ", "");
                htmlOldString = htmlOldString.replace(" ", "");

                if (htmlNewString.equals(htmlOldString)) {
                    Connector.createFile(tempFolder + "same" + i + ".html", newPDFChapters.get(i).html());
                    oldPDFChapters.remove(i);
                    newPDFChapters.remove(i);
                }
            }
            HashMap<String, HashMap<Integer, Document>> returnHashMap = new HashMap<>();

            returnHashMap.put("old", oldPDFChapters);
            returnHashMap.put("new", newPDFChapters);

            System.out.println("Hard compare done");
            System.out.println("Chapters remain to compare: " + oldPDFChapters.size() + " old chapter and " + newPDFChapters.size()
                    + " new chapter \n");
    }
}
