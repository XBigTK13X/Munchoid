grep -r -I Exception ./game/logs | wc -l | xargs -I % echo "% exceptions"
grep -r -I WIN ./game/logs | wc -l | xargs -I % echo "% wins"
grep -r -I LOSE ./game/logs | wc -l | xargs -I % echo "% losses"
python script/stats.py ./game/logs/
ls game | grep hs_ | wc -l | xargs -I % echo "Java crashed % times. Probably because it ran out of memory."
