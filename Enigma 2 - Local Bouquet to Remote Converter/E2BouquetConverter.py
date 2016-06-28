import HTTP
import xml.sax
import urllib
import codecs

'''
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
from E2WebIFBouquetReader import E2WebIFBouquetReader

class E2BouquetConverter(object):
    '''
    classdocs
    '''
        
    def __init__(self):
        '''
        Constructor
        '''

    
    def convert(self, ip, raw_port, transcoding_port, path="."):
        '''
        convertor
        '''
        path = "/etc/enigma2/";
        data = HTTP.Client().get(ip, 80, "/web/getallservices");
        handler = E2WebIFBouquetReader();
        xml.sax.parseString(data[data.find("<?xml"):data.find("</e2servicelistrecursive>")+25],handler);
        bouquet_list = codecs.open(path+"bouquets.tv", "w", "utf-8");
        bouquet_list.write("#NAME Bouquets (TV)"+"\n");
        bouquet_list.write("#SERVICE 1:7:1:0:0:0:0:0:0:0:FROM BOUQUET \"userbouquet.favourites.tv\" ORDER BY bouquet"+"\n");
        
        for bouquet in handler.getBouquets():
            chars_to_remove = ['.', '!', '?',' ','(',')']
            bouquet_name = "";
            
            for char in bouquet.getName().lower():
                if not( char in chars_to_remove) :
                    bouquet_name += char;
                else:
                    bouquet_name += "_";
            
            bouquet_file_name = "userbouquet."+bouquet_name+".tv";
            bouquet_list.write("#SERVICE 1:7:1:0:0:0:0:0:0:0:FROM BOUQUET \"" +bouquet_file_name+"\" ORDER BY bouquet"+"\n");
            
            bouquet_channel_list = codecs.open(path+bouquet_file_name, "w","utf-8");
            bouquet_channel_list.write("#NAME "+bouquet.getName()+"\n");
            
            for channel in bouquet.getChannels():
                if channel.isHD():
                    port = transcoding_port;
                else:
                    port = raw_port
    
                bouquet_channel_list.write("#SERVICE 4097:0:1:0:0:0:0:0:0:0:" + urllib.quote("http://"+ip+":"+port+"/"+channel.getRefernce()) + ":" + channel.getName()+"\n");
                bouquet_channel_list.write("#DESCRIPTION " + channel.getName()+"\n");
                            
        bouquet_channel_list.close();
        bouquet_list.close();