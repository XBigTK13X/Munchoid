target=${1-"./game/logs"}
grep -r -I Exception $target | wc -l | xargs -I % echo "% exceptions"
grep -r -I WIN $target | wc -l | xargs -I % echo "% wins"
grep -r -I LOSE $target | wc -l | xargs -I % echo "% losses"
python script/stats.py $target
ls game | grep hs_ | wc -l | xargs -I % echo "Java crashed % times. Probably because it ran out of memory."
