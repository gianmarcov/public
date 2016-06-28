import socket 
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

class Client(object):
    '''
    classdocs
    '''
    _socket =  {}

    def __init__(self):
        '''
        Constructor
        '''
        self._socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM);


    def get(self,host,port,uri):
        self._socket.connect((host,port))
        request = "GET ";
        request += uri + " HTTP/1.1\r\n";
        request +="Host: "+ host + ":" + str(port) +"\r\n";
        request +="Accept: */*\r\n";
        request +="Connection: close\r\n";
        request +="\r\n";
        self._socket.send(request);
        ''' 8KB Buffer'''
        recv_len = 1;
        response = "";
        while recv_len > 0:
            recv = self._socket.recv(8*1024);
            recv_len = len(recv);
            response += recv
        
        self._socket.close();
        return response;






         
