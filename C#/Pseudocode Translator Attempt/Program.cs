using System;
using System.IO;
using System.Linq;
using System.Collections.Generic;

namespace Pseudocode_Translator
{
    class Program
    {

        static List<List<string>> Individual_Words(string path)
        {
            
            List<string> lines = File.ReadAllLines(path).ToList();
            List<string> words = new List<string>();

            //Making the list of lines    into a list of lists of words
            List<List<string>> split_lines = new List<List<string>>();

            for (int i = 0; i < lines.ToArray().Length; i++)
            {
                words = lines[i].Split(" ").ToList();
                split_lines.Add(words);
            }
            return split_lines;
        }
        // This function is used to find what construct a line is
        static string Find_Construct(List<string> split_line)
        {
            string cSharp_Line_Out = "";

            //Checks for the Console.WriteLine(), uses two identical extensive switch case things in the else and catch due to my lack of knowledge :3
            try
            {
                if (split_line[0].Trim().Substring(0, 5) == "print")
                {
                    cSharp_Line_Out = "Console.WriteLine";
                }
                else
                {
                    switch (split_line[0].Trim())
                    {
                        case "for":
                            cSharp_Line_Out = "for ";
                            break;
                        case "while":
                            cSharp_Line_Out = "while ";
                            break;
                        case "do":
                            cSharp_Line_Out = "do ";
                            break;
                        case "if":
                            cSharp_Line_Out = "if ";
                            break;
                        case "else if":
                            cSharp_Line_Out = "else if ";
                            break;
                        case "endif":
                            cSharp_Line_Out = "}";
                            break;
                        case "else":
                            cSharp_Line_Out = "else:";
                            break;
                        case "next":
                            cSharp_Line_Out = "}";
                            break;
                        case "endwhile":
                            cSharp_Line_Out = "}";
                            break;
                        case "until":
                            cSharp_Line_Out = "while ";
                            break;
                        case "":
                            cSharp_Line_Out = "";
                            break;
                        default:
                            cSharp_Line_Out = split_line[0].Trim();
                            break;
                    }
                }
            }
            catch (Exception) 
            {
                switch (split_line[0].Trim())
                {
                    case "for":
                        cSharp_Line_Out = "for ";
                        break;
                    case "while":
                        cSharp_Line_Out = "while ";
                        break;
                    case "do":
                        cSharp_Line_Out = "do ";
                        break;
                    case "if":
                        cSharp_Line_Out = "if ";
                        break;
                    case "else if":
                        cSharp_Line_Out = "else if ";
                        break;
                    case "endif":
                        cSharp_Line_Out = "}";
                        break;
                    case "else":
                        cSharp_Line_Out = "else:";
                        break;
                    case "next":
                        cSharp_Line_Out = "}";
                        break;
                    case "endwhile":
                        cSharp_Line_Out = "}";
                        break;
                    case "until":
                        cSharp_Line_Out = "} while ";
                        break;
                    case "":
                        cSharp_Line_Out = "";
                        break;
                    default:
                        cSharp_Line_Out = split_line[0].Trim();
                        break;
                }
            }

            return cSharp_Line_Out;
        }
        
        static string Find_Selection_Condition(List<string> split_line, string cSharp_Line_In)
        {
            string cSharp_Line_Out = "";
            if (cSharp_Line_In == "if ")
            {
                cSharp_Line_Out = $"if ({split_line[1]} {split_line[2]}";
                try
                {
                    for (int i = 3; i < split_line.Count; i++)
                        cSharp_Line_Out = cSharp_Line_Out + " " + split_line[i];
                }
                catch (Exception) { }
            }
            else if (cSharp_Line_In == "else if ")
            {
                cSharp_Line_Out = $"else if ({split_line[1]} {split_line[2]}";
                try
                {
                    for (int i = 3; i < split_line.Count; i++)
                        cSharp_Line_Out = cSharp_Line_Out + " " + split_line[i];
                }
                catch (Exception) { }
            }
            else
            {
                cSharp_Line_Out = "else {";
                return cSharp_Line_Out;
            }
            cSharp_Line_Out = cSharp_Line_Out + ") {";
            return cSharp_Line_Out;
        }
        static string Find_Loop_Condition(List<string> split_line, string cSharp_Line_In)
        {
            string cSharp_Line_Out = "";
            if (cSharp_Line_In == "for ")
            {
                cSharp_Line_Out = $"for (int {split_line[1]} = {split_line[3]}; {split_line[1]}";
                if (Convert.ToInt16(split_line[3]) < Convert.ToInt16(split_line[5]))
                {
                    cSharp_Line_Out = cSharp_Line_Out + $" < {split_line[5]}; i++) {{";
                }
                else
                {
                    cSharp_Line_Out = cSharp_Line_Out + $" > {split_line[5]}; i++) {{";
                }

            }
            else if (cSharp_Line_In == "while ")
            {
                cSharp_Line_Out = $"while ({split_line[1]} {split_line[2]} {split_line[3]}) {{";
                //need to change this for more stupid scenarios
            }
            else if (cSharp_Line_In == "} while ")
            {
                cSharp_Line_Out = $"}} while ({split_line[1]} {split_line[2]} {split_line[3]})";
                //need to change this for more stupid scenarios
            }

            return cSharp_Line_Out;
        }

        static List<string> Indent_Corrections(List<string> cSharp_Lines_In)
        {
            List<string> cSharp_Lines_Out = cSharp_Lines_In;

            //Do indents for selection statements
            for (int i = 0; i < cSharp_Lines_Out.Count; i++)
            {
                try
                {
                    if (cSharp_Lines_Out[i].Substring(0, 2) == "if" || cSharp_Lines_Out[i].Substring(0, 7) == "else if" || cSharp_Lines_Out[i].Substring(0, 2) == "else")
                    {
                        for (int j = i + 1; j < cSharp_Lines_Out.Count; j++)
                        {
                            if (cSharp_Lines_Out[j] == "}")
                                break;
                            cSharp_Lines_Out[j] = "    " + cSharp_Lines_Out[j];
                        }
                    }
                }
                catch (Exception) { }
            }

            //Do indents for iteration statements
            for (int i = 0; i < cSharp_Lines_Out.Count; i++) 
            {
                try
                {
                    if (cSharp_Lines_Out[i].Substring(0, 3) == "for" || cSharp_Lines_Out[i].Substring(0, 5) == "while")
                    {
                        for (int j = i + 1; j < cSharp_Lines_Out.Count; j++)
                        {
                            if (cSharp_Lines_Out[j] == "}")
                                break;
                            cSharp_Lines_Out[j] = "    " + cSharp_Lines_Out[j];
                        }
                    }
                }
                catch (Exception) { }
            }
            //Fixing multiple }'s

            for (int i = 0; i < cSharp_Lines_Out.Count; i++)
            {
                try
                {
                    if (cSharp_Lines_Out[i] == "}")
                    {
                        if (cSharp_Lines_Out[i+1] == "}")
                        {
                            cSharp_Lines_Out[i] = "    " + cSharp_Lines_Out[i];
                            if (cSharp_Lines_Out[i + 2] == "}")
                            {
                                cSharp_Lines_Out[i] = "    " + cSharp_Lines_Out[i];
                                cSharp_Lines_Out[i + 1] = "    " + cSharp_Lines_Out[i + 1];
                                if (cSharp_Lines_Out[i + 3] == "}")
                                {
                                    cSharp_Lines_Out[i] = "    " + cSharp_Lines_Out[i];
                                    cSharp_Lines_Out[i + 1] = "    " + cSharp_Lines_Out[i + 1];
                                    cSharp_Lines_Out[i + 2] = "    " + cSharp_Lines_Out[i + 2];
                                }
                            }
                        }
                    }
                }
                catch (Exception) { }
            }
            return cSharp_Lines_Out;
        }

        static void Main(string[] args)
        {
            //Finding the file we're gonna work in and reading all its context into a list, each line is an element
            string path = @"C:\Users\Lukasz\OneDrive\Praca\Coding\C#\Pseudocode Translator\Pseudo.txt";
            List<List<string>> split_lines = Individual_Words(path);
            List<string> cSharp_Lines = new List<string>();


            // This shite
            for (int i = 0; i < split_lines.Count; i++) 
            {
                string cSharp_Line = Find_Construct(split_lines[i]);
                if (cSharp_Line == "for " || cSharp_Line == "while " || cSharp_Line == "} while ")
                {
                    cSharp_Line = Find_Loop_Condition(split_lines[i], cSharp_Line);
                }

                else if (cSharp_Line == "if " || cSharp_Line == "else if " || cSharp_Line == "else ")
                {
                    cSharp_Line = Find_Selection_Condition(split_lines[i], cSharp_Line);
                }


                else if (cSharp_Line == "Console.WriteLine")
                {
                    cSharp_Line = cSharp_Line + split_lines[i][0].Trim().Substring(5);
                    try
                    {
                        for (int j = 1; j < split_lines[i].Count; j++)
                        {
                            cSharp_Line = cSharp_Line + " " + split_lines[1][j];
                        }
                    }
                    catch (Exception) { }
                    cSharp_Line = cSharp_Line + ";";
                }
                
                else if (cSharp_Line == "}")
                {
                    cSharp_Line = "}";
                }

                else if (cSharp_Line == "")
                {
                    cSharp_Line = "";
                }

                else
                {
                    cSharp_Line = split_lines[i][0].Trim() + " " + split_lines[i][1];
                    if (split_lines[i][2] == "input()")
                    {
                        cSharp_Line = cSharp_Line + " " + "Console.ReadLine();";
                    }
                }
                cSharp_Lines.Add(cSharp_Line);
            }
            cSharp_Lines = Indent_Corrections(cSharp_Lines);
            //foreach (string line in cSharp_Lines)
            //    Console.WriteLine(line);
            

        }
    }
}
