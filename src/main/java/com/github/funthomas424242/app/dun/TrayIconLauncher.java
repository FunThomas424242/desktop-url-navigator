package com.github.funthomas424242.app.dun;

/*-
 * #%L
 * Desktop URL Navigator
 * %%
 * Copyright (C) 2019 PIUG
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * TrayIconDemo.java
 */

import org.apache.pdfbox.io.IOUtils;

import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.*;

public class TrayIconLauncher {

    protected static void launch() {
        final TrayIconLauncher launcher = new TrayIconLauncher();
        launcher.createAndShowGUI();
    }

    private void createAndShowGUI() {
        //Check the SystemTray support
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();
        final Path iconPath = Paths.get("src/main/resources/", "images/BlaetterStapel.gif");
        final TrayIcon trayIcon;
        try {
            trayIcon = new TrayIcon(createImage(iconPath, "tray icon"));

            final SystemTray tray = SystemTray.getSystemTray();

            // Create a popup menu components
            MenuItem aboutItem = new MenuItem("About");
            CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
            CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
            Menu displayMenu = new Menu("Display");
            MenuItem errorItem = new MenuItem("Error");
            MenuItem warningItem = new MenuItem("Warning");
            MenuItem infoItem = new MenuItem("Info");
            MenuItem noneItem = new MenuItem("None");
            MenuItem exitItem = new MenuItem("Exit");

            //Add components to popup menu
            popup.add(aboutItem);
            popup.addSeparator();
            popup.add(cb1);
            popup.add(cb2);
            popup.addSeparator();
            popup.add(displayMenu);
            displayMenu.add(errorItem);
            displayMenu.add(warningItem);
            displayMenu.add(infoItem);
            displayMenu.add(noneItem);
            popup.add(exitItem);

            trayIcon.setPopupMenu(popup);
            tray.add(trayIcon);

            trayIcon.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null,
                        "This dialog box is run from System Tray");
                }
            });

            aboutItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null,
                        "This dialog box is run from the About menu item");
                }
            });

            cb1.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    int cb1Id = e.getStateChange();
                    if (cb1Id == ItemEvent.SELECTED) {
                        trayIcon.setImageAutoSize(true);
                    } else {
                        trayIcon.setImageAutoSize(false);
                    }
                }
            });

            cb2.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    int cb2Id = e.getStateChange();
                    if (cb2Id == ItemEvent.SELECTED) {
                        trayIcon.setToolTip("Sun TrayIcon");
                    } else {
                        trayIcon.setToolTip(null);
                    }
                }
            });

            ActionListener listener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    MenuItem item = (MenuItem) e.getSource();
                    //TrayIcon.MessageType type = null;
                    System.out.println(item.getLabel());
                    if ("Error".equals(item.getLabel())) {
                        //type = TrayIcon.MessageType.ERROR;
                        trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an error message", TrayIcon.MessageType.ERROR);

                    } else if ("Warning".equals(item.getLabel())) {
                        //type = TrayIcon.MessageType.WARNING;
                        trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is a warning message", TrayIcon.MessageType.WARNING);

                    } else if ("Info".equals(item.getLabel())) {
                        //type = TrayIcon.MessageType.INFO;
                        trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an info message", TrayIcon.MessageType.INFO);

                    } else if ("None".equals(item.getLabel())) {
                        //type = TrayIcon.MessageType.NONE;
                        trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an ordinary message", TrayIcon.MessageType.NONE);
                    }
                }
            };

            errorItem.addActionListener(listener);
            warningItem.addActionListener(listener);
            infoItem.addActionListener(listener);
            noneItem.addActionListener(listener);

            exitItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tray.remove(trayIcon);
                    System.exit(0);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }
    }

    //Obtain the image URL
    protected Image createImage(Path path, String description) throws IOException {
        final byte[] imageData = Files.readAllBytes(path);
        return (new ImageIcon(imageData, description)).getImage();
    }

}
