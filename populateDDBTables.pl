#!/bin/pl
###################################################################
# populateDDBTables.pl                                            #
# Author: ax56                                                    #
# This script populates the DynamoDB tables on aws with data from #
# the  DynamoDB files automatically using a perl script rather    #
# than the aws cli and a an unsecure json file run from a shell   #
# script... no offence to the aws cli, but I prefer to use perl   #
# for security purposes, plus perl is ALIVE!                      #
###################################################################
use strict;
use warnings;
use JSON;
use Data::Dumper;
use LWP::UserAgent;
my $ua = LWP::UserAgent->new;
$ua->timeout(10);
$ua->env_proxy;
### read the file into an array
my @lines = `cat configurations/commands.txt`;
### loop through the array and execute the commands
for (my $idx = 0; $idx < scalar(@lines); $idx++) {
    my $line = $lines[$idx];
    chomp($line);
    print "Executing: $line";
    system($line);
### sleep for 10 seconds to allow the table to be created
    sleep(10);
### get the table name from the command
    my @split = split(/ /, $line);
    my $table = $split[3];
### read the json file into an array
    my @json = `cat configurations/$table.json`;
### loop through the array and insert the data into the table
    foreach my $json (@json) {
        chomp($json);
        my $json_obj = decode_json($json);
        my $json_str = encode_json($json_obj);
        my $url = "http://localhost:8080/$table";
        my $req = HTTP::Request->new(POST => $url);
        $req->header('Content-Type' => 'application/json');
        $req->content($json_str);
        my $res = $ua->request($req);
        if ($res->is_success) {
            print $res->decoded_content;
        }
        else {
            print $res->status_line;
        }
    }
}


