'''
Created on 06.03.2016
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Vitelli Gianmarco
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * @Autor Vitelli Gianmarco
'''
from Plugins.Plugin import PluginDescriptor
from Screens.Screen import Screen
from Screens.MessageBox import MessageBox
from Components.Button import Button
from Components.ConfigList import ConfigList, ConfigListScreen
from Components.config import config, ConfigSubsection, getConfigListEntry, ConfigIP, ConfigYesNo
from Components.ActionMap import ActionMap
from E2BouquetConverter import E2BouquetConverter

config.plugins.bouquetconverter = ConfigSubsection();
config.plugins.bouquetconverter.ip = ConfigIP(default = [0, 0, 0, 0]);
config.plugins.bouquetconverter.transcoding = ConfigYesNo(default = True);

class BouquetConverterScreen(Screen,ConfigListScreen):
    skin = """
        <screen position="center,center" size="550,400" title="Bouquet Converter" >
            <widget name="config" position="20,10" size="510,330" scrollbarMode="showOnDemand" />
            <ePixmap name="red"    position="0,350" zPosition="4" size="140,40" pixmap="skin_default/buttons/red.png" transparent="1" alphatest="on" />
            <ePixmap name="green" position="140,350" zPosition="4" size="140,40" pixmap="skin_default/buttons/green.png" transparent="1" alphatest="on" />
            <widget name="key_red" position="0,350" zPosition="5" size="140,40" valign="center" halign="center" font="Regular;21" transparent="1" foregroundColor="white" shadowColor="black" shadowOffset="-1,-1" />
            <widget name="key_green" position="140,350" zPosition="5" size="140,40" valign="center" halign="center" font="Regular;21" transparent="1" foregroundColor="white" shadowColor="black" shadowOffset="-1,-1" />
        </screen>"""
    
    def __init__(self, session, what = None):
        Screen.__init__(self, session)
        self.session = session
        
        self.list = []
        self.list.append(getConfigListEntry("IP", config.plugins.bouquetconverter.ip))
        self.list.append(getConfigListEntry("HD Channel Transcoding", config.plugins.bouquetconverter.transcoding))
        
        ConfigListScreen.__init__(self, self.list)
        
        self["actions"] = ActionMap(["SetupActions", "ColorActions"],
        {
            "green": self.keySave,
            "red": self.keyCancel,
            "cancel": self.keyCancel
        }, -2)

        self["key_red"] = Button(_("Cancel"));
        self["key_green"] = Button(_("OK"));
        
    def keySave(self):
        if config.plugins.bouquetconverter.transcoding.value:
            E2BouquetConverter().convert(config.plugins.bouquetconverter.ip.getText(), "8001", "8002");
        else:
            E2BouquetConverter().convert(config.plugins.bouquetconverter.ip.getText(), "8001", "8001");
            
        self.session.open(
        MessageBox,
        "Bouquet converted, please restart the Enigma2",
        MessageBox.TYPE_INFO)
        self.close(self.session, True)

    def keyClose(self):
        self.close(self.session, False)

def main(session, **kwargs):
    session.open(BouquetConverterScreen);


def Plugins(path,**kwargs):
    global pluginpath
    pluginpath = path
    return PluginDescriptor(name="Bouquet Converter", description="Enigma2 Remote Bouquet Converter with/out Transcoding", where = PluginDescriptor.WHERE_PLUGINMENU, fnc=main);