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
package org.pawlost.work.html.core;

public class Convert {

    public static void main(String[] args) {
        boolean test = false;
        for (String arg : args) {
            switch (arg) {
                case "--test_mode":
                    new UserTestConvert(args);
                    test = true;
                    break;
            }
        }

        if(!test) {
            new NormalConvert(args);
        }
    }
}
