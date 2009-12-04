#!/bin/sh
vlc "$1" --sout '#rtp{port=1234,sdp=rtsp://127.0.0.1:8080/stream.sdp}'

