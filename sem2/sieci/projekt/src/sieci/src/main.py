'''
Main entry point for network experiments
'''

import logging
import experiment
import wifi

if __name__ == '__main__':
    logging.basicConfig(
                        level=logging.DEBUG, 
                        format="%(asctime)s - %(levelname)s - %(module)s(%(funcName)s):%(lineno)d - %(message)s"
                        )
    
    logging.debug('network simulator says hello :-)')
    experiment.simulate() 
    #wifi.wifiSimulation()
    pass