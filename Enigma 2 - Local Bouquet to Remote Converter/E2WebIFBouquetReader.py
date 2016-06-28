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
import xml.sax
from E2Channel import E2Channel
from E2Bouquet import E2Bouquet

class E2WebIFBouquetReader(xml.sax.ContentHandler):
    '''
    classdocs
    '''
    _isbouquet = False;
    _ischannel = False;
    _isname = False;
    
    _bouquet_refernce = None;
    _channel_refernce = None;
    
    _bouquets = [];

    def __init__(self):
        '''
        Constructor
        '''

    def startElement(self, name, attrs):
        self._isname = False;
        
        if name == "e2bouquet":
            self._bouquet_refernce = E2Bouquet();
            
            self._bouquets.append(self._bouquet_refernce);
            
            self._isbouquet = True;
            self._ischannel = False;
            return;
        elif name == "e2service":  
            self._channel_refernce = E2Channel();
            
            self._bouquet_refernce.addChannel(self._channel_refernce)
            
            self._isbouquet = False;
            self._ischannel = True;
            return;
        
        if name == "e2servicename":
            self._isname = True;
            return;
        
    
    def characters(self, content):
        if ('\n' in content) or ('\t' in content):
            return;
        
        if self._isbouquet:
            if self._isname:
                self._bouquet_refernce.setName(content);
        elif self._ischannel:
            if self._isname:
                if ("HD" in content) or ("hd" in content):
                    self._channel_refernce.setIsHD(True);
                else:
                    self._channel_refernce.setIsHD(False);
                    
                self._channel_refernce.setName(content);
                
            else:
                self._channel_refernce.setRefernce(content);
        return
    
    def getBouquets(self):
        return self._bouquets;