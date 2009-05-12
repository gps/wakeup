import os

SRC_FILE_EXTNS = ['.java', '.xml']

JAVA_LGPL = """/*
Copyright (C) 2009 Gopalkrishna Sharma.
Email: gopalkri@umich.edu / gopalkrishnaps@gmail.com

This file is part of WakeUp!.

Wake Up! is free software: you can redistribute it and/or modify
it under the terms of the Lesser GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Wake Up! is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
Lesser GNU General Public License for more details.

You should have received a copy of the Lesser GNU General Public License
along with Wake Up!.  If not, see <http://www.gnu.org/licenses/>.
*/

"""

XML_LGPL = """<!--
Copyright (C) 2009 Gopalkrishna Sharma.
Email: gopalkri@umich.edu / gopalkrishnaps@gmail.com

This file is part of WakeUp!.

Wake Up! is free software: you can redistribute it and/or modify
it under the terms of the Lesser GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Wake Up! is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
Lesser GNU General Public License for more details.

You should have received a copy of the Lesser GNU General Public License
along with Wake Up!.  If not, see <http://www.gnu.org/licenses/>.
-->
"""

XML_ENCODING_STR = """<?xml version="1.0" encoding="utf-8"?>"""

def addLGPLToFile(filename):
    lgplmsg = ""
    fin = open(filename, 'r')
    content = fin.read()
    fin.close()
    if (filename.endswith('.xml')):
        if -1 == content.find(XML_ENCODING_STR):
            content = XML_LGPL + content
        else:
            content = content.replace(XML_ENCODING_STR, XML_ENCODING_STR + "\n" + XML_LGPL)
    elif filename.endswith('.java'):
        content = JAVA_LGPL + content
    else:
        raise ValueError, "Source file LGPL message can be added only to .java and .xml files"
    fout = open(filename, 'w')
    fout.write(content)
    fout.close()

def isSrcFile(filename):
    for extn in SRC_FILE_EXTNS:
        if filename.endswith(extn):
            return True
    return False

def getAllSrcFiles(rootdir):
    files = []
    entries = os.listdir(rootdir)
    for entry in entries:
        entry = os.path.join(rootdir, entry)
        if os.path.isdir(entry):
            for f in getAllSrcFiles(entry):
                files.append(f)
        if os.path.isfile(entry) and isSrcFile(entry):
            files.append(entry)
    return files

def main():
    for srcfile in getAllSrcFiles('.'):
        print "Adding LGPL statement to file:", srcfile
        addLGPLToFile(srcfile)

if __name__ == '__main__':
	main()