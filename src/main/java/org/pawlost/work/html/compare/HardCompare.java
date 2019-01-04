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
import org.pawlost.work.html.elements.WholeElement;

import java.util.HashMap;

//hard compare, compares whole pages
public class HardCompare implements Compare{

    private WholeElement chapters;

    public HardCompare(WholeElement chapters) {
        this.chapters = chapters;
    }

    @Override
    public WholeElement start() {
            System.out.println("Starting Hard Compare");

            for (int i = 0; i <= chapters.chaptersSize(); i++) {
                String htmlOldString = chapters.getOldDocument().html();
                String htmlNewString = chapters.getNewDocument().html();

                htmlNewString = htmlNewString.replace(" ", "");
                htmlOldString = htmlOldString.replace(" ", "");

                if (htmlNewString.equals(htmlOldString)) {
                    chapters.removeNewChapters(i);
                    chapters.removeOldChapters(i);
                }
            }

            System.out.println("Hard compare done");
            System.out.println("Chapters remain to compare: " + chapters.getOldChapters().size() + " old chapter and " + chapters.getNewChapters().size()
                    + " new chapter \n");
        return chapters;
    }
}
