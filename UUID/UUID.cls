VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "UUID"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
'******************************************************************************
'* The MIT License (MIT)
'* 
'* Copyright (c) 2015 Vitelli Gianmarco
'* 
'* Permission is hereby granted, free of charge, to any person obtaining a copy
'* of this software and associated documentation files (the "Software"), to deal
'* in the Software without restriction, including without limitation the rights
'* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
'* copies of the Software, and to permit persons to whom the Software is
'* furnished to do so, subject to the following conditions:
'* 
'* The above copyright notice and this permission notice shall be included in all
'* copies or substantial portions of the Software.
'* 
'* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
'* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
'* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
'* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
'* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
'* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
'* SOFTWARE.
'*
'* VBA UUID Type 1 Generator Class
'******************************************************************************
Dim last_clock_sequence As Double
Dim MacAdress As String

Private Sub Class_Initialize()
    last_clock_sequence = 0
    MacAdress = GetMacAddressWithoutDots
End Sub

Public Function createUUIDType1() As String
Dim UUID As String
Dim binary As String
Dim time_low As Double
Dim time_hi_and_version As Double
Dim clock_seq_low As Double
Dim clock_seq_hi_and_reserved As Double
Dim TYPE1_UUID_VERSION As Double
Dim currentTimestamp As Double
Dim clock_sequence As Double
Dim clock_seq_low_and_clock_seq_hi_and_reserved As Double

TYPE1_UUID_VERSION = 1 'UUID Version
currentTimestamp = Timestamp()
clock_sequence = getClockSequence

If (last_clock_sequence = clock_sequence) Then
    clock_sequence = clock_sequence + 1
End If

last_clock_sequence = clock_sequence

binary = DoubleToBinaryString(currentTimestamp)

time_low = BinaryStringToDouble(BinaryAnd(binary, "11111111111111111111111111111111"))
time_mid = BinaryStringToDouble(BinaryAnd(LogicalBinaryRightShift(binary, 32), "1111111111111111"))
time_hi_and_version = BinaryStringToDouble(BinaryOr(BinaryAnd(LogicalBinaryRightShift(binary, 48), "111111111111"), "1000000000000"))

clock_seq_low = BinaryStringToDouble(BinaryAnd(DoubleToBinaryString(clock_sequence), "0000000011111111"))
clock_seq_hi_and_reserved = BinaryStringToDouble(BinaryOr(LogicalBinaryRightShift(BinaryAnd(DoubleToBinaryString(clock_sequence), "11111100000000"), 8), "10000000"))

clock_seq_low_and_clock_seq_hi_and_reserved = BinaryStringToDouble(BinaryOr(LogicalBinaryLeftShift(DoubleToBinaryString(clock_seq_hi_and_reserved), 8), DoubleToBinaryString(clock_seq_low)))

UUID = DoubleToHEX(CDec(time_low)) & "-" & DoubleToHEX(CDec(time_mid)) & "-" & DoubleToHEX(CDec(time_hi_and_version)) & "-" & DoubleToHEX(CDec(clock_seq_low_and_clock_seq_hi_and_reserved)) & "-" & MacAdress
createUUIDType1 = UUID
End Function
Public Function Timestamp() As Double
    Timestamp = ((DateDiff("s", "15/10/1582", Now) - 3600) * 10000000) 'UTC+1 to UTC conversion, change the 3600 to your Country specific Timezone
End Function
' Generate random number between 0 and 16383 (14bit)
Public Function getClockSequence() As Integer
    getClockSequence = ((16383 - 0 + 1) * Rnd + 0)
End Function
'Special Function to convert to Binary
Public Function DoubleToBinaryString(ByVal d As Double) As String
    Dim binary As String
    Dim svalue As String
    Dim dvalue As String
    Dim count As Integer
    dvalue = d
    
    While dvalue <> 0
        'Emulate modulo
        dvalue = dvalue / 2
        svalue = CStr(CDec(dvalue))
        If (InStr(1, svalue, ".", vbTextCompare) > 0) Then
            binary = "1" & binary
            dvalue = CDbl(Split(svalue, ".")(0)) 'Remove numbers before decimal
        Else
            binary = "0" & binary
        End If
    Wend
    'Fill with null byte
    count = Len(binary)
    While count <> 64
        binary = "0" & binary
        count = count + 1
    Wend
    
    DoubleToBinaryString = binary
    End Function
'Special Function to convert to Binary
Public Function BinaryStringToDouble(ByVal binary As String) As Double
    Dim svalue As String
    Dim dvalue As Double
    Dim index As Integer
    Dim exponent As Integer
    exponent = Len(binary) - 1
    dvalue = 0

    For i = 1 To Len(binary)
        dvalue = dvalue + (CInt(Mid(binary, i, 1)) * (2 ^ exponent))
        exponent = exponent - 1
    Next
    
    BinaryStringToDouble = dvalue
End Function
Public Function LogicalBinaryRightShift(ByVal binary As String, index As Integer) As String
'>
Dim count As Integer
count = Len(binary) - index
    For i = 1 To index
        binary = "0" & binary
    Next
    binary = Left(binary, 64)
    LogicalBinaryRightShift = binary
End Function
Public Function LogicalBinaryLeftShift(ByVal binary As String, index As Integer) As String
'<
Dim count As Integer
count = Len(binary) - index
    For i = 1 To index
        binary = binary & "0"
    Next
    binary = Right(binary, 64)
    LogicalBinaryLeftShift = binary
End Function
Public Function BinaryAnd(ByVal binary0 As String, ByVal binary1 As String) As String
'&
Dim binary As String
Dim length0 As Integer
Dim length1 As Integer
Dim char0 As String
Dim char1 As String
length0 = Len(binary0)
length1 = Len(binary1)

    While length1 <> 0
        char0 = Mid(binary0, length0, 1)
        char1 = Mid(binary1, length1, 1)
        
        If (StrComp(char0, char1, vbTextCompare) = 0) Then
            If (StrComp(char0, "1", vbTextCompare) = 0) Then
                binary = "1" & binary
            Else
                binary = "0" & binary
            End If
        Else
            binary = "0" & binary
        End If
        
        length0 = length0 - 1
        length1 = length1 - 1
    Wend
    
    'Fill with null byte
    For i = 1 To (length0 - length1)
        binary = "0" & binary
    Next
    
    BinaryAnd = binary
End Function
Public Function BinaryOr(ByVal binary0 As String, ByVal binary1 As String) As String
'|
Dim binary As String
Dim length0 As Integer
Dim length1 As Integer
Dim char0 As String
Dim char1 As String
length0 = Len(binary0)
length1 = Len(binary1)

    While length1 <> 0
        char0 = Mid(binary0, length0, 1)
        char1 = Mid(binary1, length1, 1)
        
        If (StrComp(char0, "1", vbTextCompare) = 0 Or StrComp(char1, "1", vbTextCompare) = 0) Then
            binary = "1" & binary
        Else
            binary = "0" & binary
        End If
        
        length0 = length0 - 1
        length1 = length1 - 1
    Wend
    
    'Fill with null byte
    For i = 1 To (length0 - length1)
        binary = "0" & binary
    Next
    
    BinaryOr = binary
End Function
Public Function DoubleToHEX(ByVal d As Double) As String
    Dim Hex As String
    Dim HexCode As String
    Dim dvalueb As Double
    Dim dvaluea As Double
    Dim svalue As String
    Dim reminder As Double
    HexCode = "123456789ABCDEF"
    dvalueb = d
    While dvalueb <> 0
        'Emulate modulo
        dvaluea = dvalueb / 16
        svalue = CStr(CDec(dvaluea))
        If (InStr(1, svalue, ".", vbTextCompare) > 0) Then
            dvaluea = CDbl(Split(svalue, ".")(0)) 'Remove numbers before decimal
            reminder = dvalueb - (dvaluea * 16)
            Hex = Mid(HexCode, reminder, 1) & Hex
        Else
            Hex = "0" & Hex
        End If
        dvalueb = dvaluea
    Wend
    
    DoubleToHEX = Hex
End Function
Public Function GetMacAddressWithoutDots()
    Dim objNetwork As Object
    Dim strNetworkSql As String
    Dim strMacAdr As String

    strNetworkSql = "SELECT * FROM Win32_NetworkAdapter WHERE MACAddress IS NOT NULL"
    For Each objNetwork In GetObject("winmgmts:").ExecQuery(strNetworkSql)
        strMacAdr = objNetwork.MACAddress
        If strMacAdr <> "" Then Exit For
    Next

    GetMacAddressWithoutDots = Replace(strMacAdr, ":", "")
End Function
